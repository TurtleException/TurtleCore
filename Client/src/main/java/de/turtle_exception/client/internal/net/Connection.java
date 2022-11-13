package de.turtle_exception.client.internal.net;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.turtle_exception.client.internal.NetworkAdapter;
import de.turtle_exception.client.internal.net.message.Conversation;
import de.turtle_exception.client.internal.net.message.Route;
import de.turtle_exception.client.internal.net.packets.CompiledPacket;
import de.turtle_exception.client.internal.net.packets.Packet;
import de.turtle_exception.client.internal.util.Checks;
import de.turtle_exception.client.internal.util.Worker;
import de.turtle_exception.client.internal.util.crypto.Encryption;
import de.turtle_exception.client.internal.util.logging.NestedLogger;
import kotlin.NotImplementedError;
import org.jetbrains.annotations.NotNull;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.security.auth.login.LoginException;
import java.io.*;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class Connection {
    private final NetworkAdapter adapter;
    private final NestedLogger logger;

    private final Socket socket;
    private final PrintWriter out;
    private final BufferedReader in;

    private final String pass;

    private final Worker receiver;

    public enum Status { INIT, LOGIN, CONNECTED, DISCONNECTED;}
    private Status status;

    public static final long CONVERSATION_TIMEOUT_EXTRA = 10000L;
    private final ConcurrentHashMap<Long, Conversation> conversations = new ConcurrentHashMap<>();

    public Connection(@NotNull NetworkAdapter adapter, @NotNull Socket socket, @NotNull Handshake handshake, String pass) throws IOException, LoginException {
        this.status = Status.INIT;

        this.adapter = adapter;
        this.logger = new NestedLogger("CON#" + socket.getInetAddress() + ":" + socket.getPort(), adapter.getLogger());

        this.socket = socket;
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        this.status = Status.LOGIN;
        handshake.start();
        handshake.await(10, TimeUnit.SECONDS);
        this.status = Status.CONNECTED;

        this.pass = handshake.getPass() != null ? handshake.getPass() : pass;

        if (this.pass == null)
            throw new LoginException("Pass must either be provided by Handshake or not be null.");

        this.receiver = new Worker(() -> status == Status.CONNECTED, () -> {
            try {
                this.receive(in.readLine());
            } catch (IOException e) {
                logger.log(Level.WARNING, "Could not read input", e);
            }
            this.clearTimeoutConversations();
        });
    }

    public boolean stop(boolean notify) {
        if (notify) {
            // TODO: notify the other side
        }

        this.status = Status.DISCONNECTED;
        this.receiver.interrupt();
        try {
            this.socket.close();
            return true;
        } catch (IOException e) {
            this.logger.log(Level.WARNING, "Could not close connection!", e);
            return false;
        }
    }

    /* - - - */

    public synchronized void send(@NotNull CompiledPacket packet) {
        try {
            // TODO: this can't be the most efficient way to do this, right? right...?
            this.out.println(new String(packet.getBytes()));
        } catch (Error e) {
            logger.log(Level.SEVERE, "Encountered an Error when attempting to send a packet", e);
        } catch (Throwable t) {
            logger.log(Level.WARNING, "Encountered an Exception when attempting to send a packet", t);
        }
    }

    /* - - - */

    public @NotNull CompletableFuture<Message> send(@NotNull Message message) {
        Conversation conv = this.getConversation(message);

        // send message
        this.send(new Gson().toJson(message.toJson()));

        // update conversation & return CompletableFuture for the response
        return conv.append(message);
    }

    public void receive(@NotNull Message message) {
        Conversation conv = this.getConversation(message);

        // store this value, so we can respond to the conversation AFTER adding the received message
        boolean isNewConv = conv.isEmpty();

        // update conversation
        conv.append(message);

        // stop here if the incoming message was a response
        if (!isNewConv) return;

        Route route = message.getRoute();

        if (Checks.equalsAny(route, Route.ERROR, Route.HEARTBEAT_ACK, Route.OK)) {
            this.logger.log(Level.WARNING, "Received dangling " + route.name() + ": " + message.getJson());
            conv.close();
            return;
        }

        if (route == Route.QUIT) {
            this.logger.log(Level.WARNING, "Received QUIT route.");
            this.stop(false);
            return;
        }

        if (route == Route.HEARTBEAT) {
            new HeartbeatAcknowledgeAction(message).queue();
            return;
        }

        if (route == Route.DATA) {
            new DataRequestAction(message).queue();
            return;
        }

        throw new NotImplementedError("Unknown route: " + route.name());
    }

    private @NotNull Conversation getConversation(@NotNull Message message) {
        long conversationId = message.getConversation();
        Conversation conversation = this.conversations.get(conversationId);

        if (conversation == null) {
            conversation = new Conversation(this, conversationId);
            this.conversations.put(conversationId, conversation);
        }

        return conversation;
    }

    private void clearTimeoutConversations() {
        conv:
        for (Conversation conversation : Set.copyOf(this.conversations.values())) {
            for (Long time : conversation.getMessages().keySet()) {
                if (time + CONVERSATION_TIMEOUT_EXTRA >= System.currentTimeMillis()) {
                    // this conversation has a message that is not yet timed out
                    continue conv;
                }
            }
            // this conversation only has messages that are already timed out
            this.conversations.remove(conversation.getId());
        }
    }

    public void closeConversation(@NotNull Conversation conversation) {
        this.conversations.remove(conversation.getId(), conversation);
    }

    /* - - - */

    public NetworkAdapter getAdapter() {
        return adapter;
    }

    public NestedLogger getLogger() {
        return logger;
    }
}
