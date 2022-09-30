package de.turtle_exception.core.netcore.net;

import de.turtle_exception.core.netcore.TurtleCore;
import de.turtle_exception.core.netcore.crypto.Encryption;
import de.turtle_exception.core.netcore.net.message.InboundMessage;
import de.turtle_exception.core.netcore.net.message.MessageParser;
import de.turtle_exception.core.netcore.net.message.OutboundMessage;
import de.turtle_exception.core.netcore.util.AsyncLoopThread;
import de.turtle_exception.core.netcore.util.logging.NestedLogger;
import org.jetbrains.annotations.NotNull;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.concurrent.*;
import java.util.logging.Level;

public abstract class NetworkAdapter {
    private   final TurtleCore core;
    protected final NestedLogger logger;

    protected final CallbackRegistrar callbackRegistrar;
    protected ScheduledThreadPoolExecutor executor;
    protected AsyncLoopThread receiver;
    protected ConnectionStatus status;

    /** The username to log in with */
    protected final String login;
    /** The password used for en-/decryption */
    protected final String pass;

    protected NetworkAdapter(TurtleCore core, NestedLogger logger, @NotNull String login, @NotNull String pass) {
        this.core = core;
        this.logger = logger;

        this.executor = new ScheduledThreadPoolExecutor(4, (r, executor) -> logger.log(Level.WARNING, "An outbound message was rejected by the executor: " + r));
        this.callbackRegistrar = new CallbackRegistrar();

        this.login = login;
        this.pass  = pass;
    }

    /* - - - */

    public abstract void stop() throws IOException;

    /* - - - */

    protected final void handleOutbound(@NotNull OutboundMessage msg) {
        String message = "";
        try {
            message = MessageParser.parse(msg);
            message = Encryption.encrypt(message, pass);
            this.send(message);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidAlgorithmParameterException |
                 NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            logger.log(Level.WARNING, "Could not encrypt outbound message: " + message, e);
        }
    }

    protected abstract void send(@NotNull String msg);

    protected final void handleInbound(@NotNull String msg) {
        String decryptedMessage = "";
        try {
            decryptedMessage = Encryption.decrypt(msg, pass);
        } catch (InvalidAlgorithmParameterException | NoSuchPaddingException | IllegalBlockSizeException |
                 NoSuchAlgorithmException | InvalidKeySpecException | BadPaddingException | InvalidKeyException e) {
            logger.log(Level.WARNING, "Could not decrypt inbound message.", e);
        }

        try {
            InboundMessage message = MessageParser.parse(core, decryptedMessage);
            this.submit(message);
        } catch (IllegalArgumentException e) {
            logger.log(Level.WARNING, "Could not parse inbound message: " + decryptedMessage, e);
        } catch (RejectedExecutionException e) {
            logger.log(Level.WARNING, "Could not submit inbound message: " + decryptedMessage, e);
        }
    }

    /* - - - */

    public final void submit(@NotNull OutboundMessage message) throws RejectedExecutionException {
        if (this.getStatus() != ConnectionStatus.LOGGED_IN)
            throw new RejectedExecutionException("The NetworkAdapter has been stopped!");

        if (message.getRoute().terminating())
            callbackRegistrar.unregister(message.getRoute().callbackCode());
        else
            callbackRegistrar.register(message);

        // do send
        this.handleOutbound(message);

        // await response until deadline
        CompletableFuture.runAsync(() -> {
            while (System.currentTimeMillis() <= message.getDeadline()) {
                if (message.cancelled)
                    throw new CancellationException();
                if (message.done) {
                    callbackRegistrar.unregister(message.getRoute().callbackCode());
                    return;
                }
            }
            throw new CompletionException(new TimeoutException());
        }, executor);
    }

    public final void submit(@NotNull InboundMessage message) throws RejectedExecutionException {
        if (this.getStatus() != ConnectionStatus.LOGGED_IN)
            throw new RejectedExecutionException("The NetworkAdapter has been stopped!");

        OutboundMessage initialMsg = callbackRegistrar.getOutboundMessage(message.getRoute().callbackCode());

        // this will also unregister the callback code
        if (initialMsg != null)
            initialMsg.handleResponse(message);

        // overwrite the current message for the callback code
        if (!message.getRoute().terminating())
            callbackRegistrar.register(message);

        core.getRouteManager().getRouteFinalizer(message.getRoute().command()).accept(message);
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
