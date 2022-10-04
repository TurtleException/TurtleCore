package de.turtle_exception.core.core.net;

import de.turtle_exception.core.core.TurtleCore;
import de.turtle_exception.core.core.crypto.Encryption;
import de.turtle_exception.core.core.net.message.InboundMessage;
import de.turtle_exception.core.core.net.message.Message;
import de.turtle_exception.core.core.net.message.MessageParser;
import de.turtle_exception.core.core.net.message.OutboundMessage;
import de.turtle_exception.core.core.net.route.Route;
import de.turtle_exception.core.core.net.route.RouteErrors;
import de.turtle_exception.core.core.net.route.Routes;
import de.turtle_exception.core.core.util.AsyncLoopThread;
import de.turtle_exception.core.core.util.logging.NestedLogger;
import org.jetbrains.annotations.NotNull;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;
import java.util.concurrent.*;
import java.util.logging.Level;

public abstract class NetworkAdapter {
    public static final String LOGGED_IN = "LOGGED IN";

    private   final TurtleCore core;
    protected final NestedLogger logger;

    protected final ConcurrentHashMap<Long, Message> conversations = new ConcurrentHashMap<>();

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

        this.login = login;
        this.pass  = pass;
    }

    /* - - - */

    public abstract void stop() throws IOException;

    protected abstract void quit() throws IOException;

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
        } catch (Throwable t) {
            logger.log(Level.WARNING, "Could not handle outbound message: " + message, t);
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
        } catch (Throwable t) {
            logger.log(Level.WARNING, "Could not handle inbound message.", t);
        }

        try {
            InboundMessage message = MessageParser.parse(core, this, decryptedMessage);
            this.submit(message);
        } catch (IllegalArgumentException e) {
            logger.log(Level.WARNING, "Could not parse inbound message: " + decryptedMessage, e);
        } catch (RejectedExecutionException e) {
            logger.log(Level.WARNING, "Could not submit inbound message: " + decryptedMessage, e);
        } catch (Throwable t) {
            logger.log(Level.WARNING, "Could not handle inbound message: " + decryptedMessage, t);
        }
    }

    /* - - - */

    public final void submit(@NotNull OutboundMessage message) throws RejectedExecutionException {
        if (this.getStatus() != ConnectionStatus.LOGGED_IN)
            throw new RejectedExecutionException("The NetworkAdapter has been stopped!");

        if (message.isTerminating())
            conversations.remove(message.getConversation());
        else
            conversations.put(message.getConversation(), message);

        // do send
        this.handleOutbound(message);

        // await response until deadline
        CompletableFuture.runAsync(() -> {
            while (System.currentTimeMillis() <= message.getDeadline()) {
                if (message.cancelled)
                    throw new CancellationException();
                if (message.done)
                    return;
            }
            throw new CompletionException(new TimeoutException());
        }, executor);
    }

    public final void submit(@NotNull InboundMessage message) throws RejectedExecutionException {
        if (this.getStatus() != ConnectionStatus.LOGGED_IN)
            throw new RejectedExecutionException("The NetworkAdapter has been stopped!");

        if (conversations.get(message.getConversation()) instanceof OutboundMessage initialMsg) {
            initialMsg.handleResponse(message);
            conversations.remove(message.getConversation(), initialMsg);
        } else {
            if (!this.handleIncomingRequest(message))
                message.respond(RouteErrors.NOT_SUPPORTED.compile(), core.getDefaultTimeoutOutbound(), in -> { });
        }

        // overwrite the current message for the callback code
        if (!message.isTerminating())
            conversations.put(message.getConversation(), message);

        // TODO: completely remove finalizers
    }

    /* - - - */

    // TODO: replace this with something less weird (also implement reserving codes)
    public long newConversation() {
        Long code = null;
        Random random = new Random();
        while (code == null) {
            code = random.nextLong();

            if (conversations.containsKey(code))
                code = null;
        }
        return code;
    }

    /* - - - */

    // TODO: handle inbound request
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    protected boolean handleIncomingRequest(@NotNull InboundMessage msg) {
        Route route = msg.getRoute().route();

        if (route.equals(Routes.OK)) return true;
        if (route.equals(Routes.QUIT)) {
            logger.log(Level.INFO, "Received QUIT message.");
            try {
                this.quit();
            } catch (IOException e) {
                logger.log(Level.WARNING, "Could not close socket properly.", e);
            }
            return true;
        }
        if (route.equals(Routes.ERROR)) {
            logger.log(Level.WARNING, "Dangling ERROR: " + msg.getRoute().content());
            return true;
        }

        return false;
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
