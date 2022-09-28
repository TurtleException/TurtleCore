package de.turtle_exception.core.netcore.net;

import de.turtle_exception.core.netcore.TurtleCore;
import de.turtle_exception.core.netcore.net.message.InboundMessage;
import de.turtle_exception.core.netcore.net.message.MessageParser;
import de.turtle_exception.core.netcore.net.message.OutboundMessage;
import de.turtle_exception.core.netcore.util.AsyncLoopThread;
import de.turtle_exception.core.netcore.util.logging.NestedLogger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.*;
import java.util.logging.Level;

public abstract class NetworkAdapter {
    private   final TurtleCore core;
    protected final NestedLogger logger;

    protected final CallbackRegistrar callbackRegistrar;
    protected ScheduledThreadPoolExecutor executor;
    protected AsyncLoopThread receiver;
    protected ConnectionStatus status;

    protected NetworkAdapter(TurtleCore core, NestedLogger logger) {
        this.core = core;
        this.logger = logger;

        this.executor = new ScheduledThreadPoolExecutor(4, (r, executor) -> logger.log(Level.WARNING, "An outbound message was rejected by the executor: " + r));
        this.callbackRegistrar = new CallbackRegistrar();
    }

    /* - - - */

    public abstract void stop() throws IOException;

    /* - - - */

    protected final void handleOutbound(@NotNull OutboundMessage msg) {
        String message = MessageParser.parse(msg);
        // TODO: encrypt
        this.send(message);
    }

    protected abstract void send(@NotNull String msg);

    protected final void handleInbound(@NotNull String msg) {
        // TODO: decrypt
        try {
            InboundMessage message = MessageParser.parse(core, msg);
            this.submit(message);
        } catch (IllegalArgumentException e) {
            logger.log(Level.WARNING, "Could not parse inbound message: " + msg, e);
        } catch (RejectedExecutionException e) {
            logger.log(Level.WARNING, "Could not submit inbound message: " + msg, e);
        }
    }

    /* - - - */

    public final @NotNull CompletableFuture<OutboundMessage> submit(@NotNull OutboundMessage message) throws RejectedExecutionException {
        if (this.getStatus() != ConnectionStatus.LOGGED_IN)
            throw new RejectedExecutionException("The NetworkAdapter has been stopped!");

        if (message.getRoute().terminating())
            callbackRegistrar.unregister(message.getRoute().callbackCode());
        else
            callbackRegistrar.register(message);

        // do send
        this.handleOutbound(message);

        // await response until deadline
        return CompletableFuture.supplyAsync(() -> {
            while (System.currentTimeMillis() <= message.getDeadline()) {
                if (message.cancelled)
                    throw new CancellationException();
                if (message.done)
                    return message;
            }
            throw new CompletionException(new TimeoutException());
        }, executor);
    }

    public final void submit(@NotNull InboundMessage message) throws RejectedExecutionException {
        if (this.getStatus() != ConnectionStatus.LOGGED_IN)
            throw new RejectedExecutionException("The NetworkAdapter has been stopped!");

        OutboundMessage initialMsg = callbackRegistrar.getOutboundMessage(message.getRoute().callbackCode());
        callbackRegistrar.register(message);

        if (initialMsg != null)
            initialMsg.handleResponse(message);

        // in case the inbound message is still relevant it is still registered to the callbackRegistrar
    }

    /* - - - */

    /** A helper method for {@link NetworkAdapter#stop()}. */
    protected final void stopReceiver() {
        this.receiver.interrupt();
        this.receiver = null;
    }

    /** A helper method for {@link NetworkAdapter#stop()} */
    @SuppressWarnings("StatementWithEmptyBody")
    protected final void awaitExecutorShutdown() {
        this.executor.shutdown();

        // TODO: use #awaitTermination() instead?

        while (!this.executor.isTerminated()) { }

        this.executor = null;
    }

    public @NotNull ConnectionStatus getStatus() {
        return status;
    }
}
