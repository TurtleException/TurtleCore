package de.turtle_exception.core.net;

import de.turtle_exception.core.TurtleCore;
import de.turtle_exception.core.crypto.Encryption;
import de.turtle_exception.core.net.message.InboundMessage;
import de.turtle_exception.core.net.message.Message;
import de.turtle_exception.core.net.message.MessageParser;
import de.turtle_exception.core.net.message.OutboundMessage;
import de.turtle_exception.core.net.route.Route;
import de.turtle_exception.core.net.route.RouteErrors;
import de.turtle_exception.core.net.route.Routes;
import de.turtle_exception.core.net.route.RouteHandler;
import de.turtle_exception.core.util.AsyncLoopThread;
import de.turtle_exception.core.util.logging.NestedLogger;
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

/**
 * A NetworkAdapter is responsible for establishing and managing a connection and functions as an interface between two
 * remote parts of the application. Implementations of this class should handle encryption, {@link Route} parsing and
 * conversation caching.
 */
public abstract class NetworkAdapter {
    /** The last message that should be sent unencrypted to signal that from now on all traffic will be encrypted. */
    public static final String LOGGED_IN = "LOGGED IN";

    private   final TurtleCore core;
    protected final NestedLogger logger;

    /**
     * Conversations are basic associations between inbound and outbound messages that provide information on how or
     * where a message should be processed. Every message contains a conversation code that is randomly generated for
     * every new request (on the requesting side) and will be the same for every response or error associated with the
     * initial message.
     * @see NetworkAdapter#newConversation()
     * @see MessageParser
     */
    protected final ConcurrentHashMap<Long, Message> conversations = new ConcurrentHashMap<>();
    /**
     * Designated handlers for every non-responding route this {@link NetworkAdapter} supports.
     * @see RouteHandler
     */
    protected final ConcurrentHashMap<Route, RouteHandler> handlers = new ConcurrentHashMap<>();

    /** A simple executor to await responses to outbound messages. */
    protected ScheduledThreadPoolExecutor executor;
    /** Constantly receives data from the remote connection and passes it on to {@link NetworkAdapter#handleInbound(String)} */
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

        this.registerHandler(Routes.OK      , (netAdapter, msg) -> {
            throw new UnexpectedRouteException("Dangling OK");
        });
        this.registerHandler(Routes.QUIT    , (netAdapter, msg) -> {
            logger.log(Level.INFO, "Received QUIT message.");
            try {
                this.quit();
            } catch (IOException e) {
                logger.log(Level.WARNING, "Could not close socket properly.", e);
            }
        });
        this.registerHandler(Routes.ERROR   , (netAdapter, msg) -> {
            throw new UnexpectedRouteException("Dangling ERROR: " + msg.getRoute().content());
        });
        this.registerHandler(Routes.RESPONSE, (netAdapter, msg) -> {
            throw new UnexpectedRouteException("Dangling RESPONSE: " + msg.getRoute().content());
        });
    }

    /* - - - */

    /**
     * Closes the remote connection and stops the NetworkAdapter.
     * @throws IOException if any IO problems occur while attempting to stop.
     * @implNote This method should generally only notify the remote application and then call {@link NetworkAdapter#quit()}
     */
    public abstract void stop() throws IOException;

    /**
     * Closes the remote connection and stops the NetworkAdapter without notifying the remote application.
     * @throws IOException if any IO problems occur while attempting to quit.
     * @see NetworkAdapter#stop()
     */
    protected abstract void quit() throws IOException;

    /* - - - */

    /**
     * Takes in an {@link OutboundMessage} object, parses it to an encrypted String and passes it on to
     * {@link NetworkAdapter#send(String)} to be sent to the remote connection. If either of these operations fail it
     * will be reported to the {@link NetworkAdapter#logger} and the message will be ignored.
     * <p> This method does not handle conversations!
     * @see NetworkAdapter#submit(InboundMessage)
     */
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

    /**
     * Sends a provided String to the remote connection without processing it any further.
     * <p> This method does not handle encryption!
     * @see NetworkAdapter#handleOutbound(OutboundMessage)
     */
    protected abstract void send(@NotNull String msg);

    /**
     * Takes in an encrypted message that was received by the {@link NetworkAdapter#receiver} and attempts to decrypt it
     * and parse it to an {@link InboundMessage} object. If either of these operations fail it will be reported to the
     * {@link NetworkAdapter#logger} and the message will be ignored.
     * @param msg Encrypted message from remote connection.
     */
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

    /**
     * Submits a message to be sent to the remote connection.
     * @throws RejectedExecutionException if the {@link NetworkAdapter#executor} rejects the task to asynchronously
     *                                    await cancellation or timeout. This does not mean that the message has not
     *                                    been sent!
     */
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

    /**
     * Submits a message to be handled by the NetworkAdapter.
     * @throws RejectedExecutionException if the NetworkAdapter has been stopped.
     */
    protected final void submit(@NotNull InboundMessage message) throws RejectedExecutionException {
        if (this.getStatus() != ConnectionStatus.LOGGED_IN)
            throw new RejectedExecutionException("The NetworkAdapter has been stopped!");

        if (conversations.get(message.getConversation()) instanceof OutboundMessage initialMsg) {
            initialMsg.handleResponse(message);
            conversations.remove(message.getConversation(), initialMsg);
        } else {
            if (this.handleIncomingRequest(message)) return;
        }

        // overwrite the current message for the callback code
        if (!message.isTerminating())
            conversations.put(message.getConversation(), message);
    }

    /* - - - */

    /**
     * Requests a new conversation code. This will generate a random {@code long} value that is not yet in use.
     * <p> This method has two technical weak points that can be ignored because of their extreme improbability of occurring:
     * <ul>
     *     <li> When calling this method on multiple Threads on the same time it would be possible to get the same result
     *     on two or more calls. Due to the delay of registering the messages to the {@link NetworkAdapter#conversations}
     *     map (which happens outside of this method) it would therefore be possible for 2 unrelated messages to have the
     *     same conversation code.
     *     <li> Because this method loops until a new conversation code has been found (that is not already registered to
     *     conversation map) it would be possible for this method to loop infinitely by either generating the same random
     *     value over and over or by every possible code already being taken.
     * </ul>
     * However, for either of these scenarios to occur the exact same long value would have to be generated twice within
     * milliseconds or {@value Long#MAX_VALUE} conversations would have to be registered, which would also be impossible
     * because the {@link NetworkAdapter#conversations} has a maximum capacity of {@code 2^16}.
     */
    public long newConversation() {
        Long code = null;
        Random random = new Random();
        // this should generally only run once but you never know
        while (code == null) {
            code = random.nextLong();

            if (conversations.containsKey(code))
                code = null;
        }
        return code;
    }

    /* - - - */

    /**
     * Registers a new {@link RouteHandler} for a specific {@link Route}.
     * @param route The route.
     * @param handler The handler.
     */
    public void registerHandler(@NotNull Route route, @NotNull RouteHandler handler) {
        handlers.put(route, handler);
    }

    /**
     * This method is called by {@link NetworkAdapter#submit(InboundMessage)} when the inbound message is not a response
     * to an outbound message and therefore should be treated as an incoming request.
     * @return {@code true} if the incoming message should <b>not</b> be registered to the conversation map because it
     *         has been handled exceptionally.
     */
    protected final boolean handleIncomingRequest(@NotNull InboundMessage msg) {
        Route route = msg.getRoute().route();

        RouteHandler handler = handlers.get(route);
        if (handler != null) {
            try {
                handler.handle(this, msg);
                return false;
            } catch (UnexpectedRouteException e) {
                this.logger.log(Level.WARNING, e.getMessage());
                return true;
            } catch (Exception e) {
                msg.respond(RouteErrors.BAD_REQUEST.with(e).compile(), System.currentTimeMillis() + core.getDefaultTimeoutOutbound(), in -> { });
                return true;
            }
        } else {
            msg.respond(RouteErrors.NOT_SUPPORTED.compile(), core.getDefaultTimeoutOutbound(), in -> { });
            return true;
        }
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

    /** Provides the current status of this NetworkAdapter. */
    public @NotNull ConnectionStatus getStatus() {
        return status;
    }
}
