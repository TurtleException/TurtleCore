package de.turtle_exception.core.netcore.net;

import de.turtle_exception.core.netcore.TurtleCore;
import de.turtle_exception.core.netcore.net.message.Message;
import de.turtle_exception.core.netcore.net.message.MessageParser;
import de.turtle_exception.core.netcore.util.AsyncLoopThread;
import de.turtle_exception.core.netcore.util.logging.NestedLogger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Level;

public abstract class NetworkAdapter {
    private final TurtleCore core;
    protected final NestedLogger logger;

    protected RequestExecutor inboundExecutor;
    protected RequestExecutor outboundExecutor;

    protected final CallbackRegistrar callbackRegistrar;

    protected AsyncLoopThread receiver;

    protected ConnectionStatus status;

    protected NetworkAdapter(TurtleCore core, NestedLogger logger) {
        this.core = core;
        this.logger = logger;

        this.inboundExecutor  = new RequestExecutor((r, executor) -> { logger.log(Level.WARNING,  "An inbound message was rejected by the executor: " + r); });
        this.outboundExecutor = new RequestExecutor((r, executor) -> { logger.log(Level.WARNING, "An outbound message was rejected by the executor: " + r); });

        this.callbackRegistrar = new CallbackRegistrar();
    }

    /* - - - */

    public abstract void stop() throws IOException;

    /* - - - */

    protected final void handleOutbound(@NotNull Message msg) {
        String message = MessageParser.parse(msg);
        // TODO: encrypt
        this.send(message);
    }

    protected abstract void send(@NotNull String msg);

    protected final void handleInbound(@NotNull String msg) {
        // TODO: decrypt
        try {
            Message message = MessageParser.parse(core, msg);
            this.submitInbound(message);
        } catch (IllegalArgumentException e) {
            logger.log(Level.WARNING, "Could not parse inbound message: " + msg, e);
        } catch (RejectedExecutionException e) {
            logger.log(Level.WARNING, "Could not submit inbound message: " + msg, e);
        }
    }

    /* - - - */

    public final @NotNull CompletableFuture<Void> submitOutbound(@NotNull Message message) throws RejectedExecutionException {
        if (this.getStatus() != ConnectionStatus.LOGGED_IN)
            throw new RejectedExecutionException("The NetworkAdapter has been stopped!");

        if (message.getRoute().isTerminating())
            callbackRegistrar.unregister(message.getCallbackCode());
        else
            callbackRegistrar.register(message);

        // do send
        this.handleOutbound(message);

        return this.outboundExecutor.submit(message);
    }

    public final void submitInbound(@NotNull Message message) throws RejectedExecutionException {
        if (this.getStatus() != ConnectionStatus.LOGGED_IN)
            throw new RejectedExecutionException("The NetworkAdapter has been stopped!");

        // TODO: alert initial message if the inbound message is a response

        this.outboundExecutor.respond(message.getCallbackCode(), message);
        this.inboundExecutor.submit(message);
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
        this.inboundExecutor.shutdown();
        this.outboundExecutor.shutdown();

        while (!this.inboundExecutor.isTerminated()) { }
        while (!this.outboundExecutor.isTerminated()) { }

        this.inboundExecutor = null;
        this.outboundExecutor = null;
    }

    public @NotNull ConnectionStatus getStatus() {
        return status;
    }
}
