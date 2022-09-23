package de.turtle_exception.core.internal.net.client;

import de.turtle_exception.core.api.TurtleClient;
import de.turtle_exception.core.internal.TurtleClientImpl;
import de.turtle_exception.core.internal.net.ActionImpl;
import de.turtle_exception.core.internal.net.ActionParser;
import de.turtle_exception.core.internal.net.NetworkAdapter;
import de.turtle_exception.core.internal.net.Route;
import de.turtle_exception.core.internal.net.action.AnswerableAction;
import de.turtle_exception.core.internal.net.action.RemoteActionImpl;
import de.turtle_exception.core.internal.net.action.VoidAction;
import de.turtle_exception.core.internal.net.server.VirtualClient;
import de.turtle_exception.core.internal.util.AsyncLoopThread;
import de.turtle_exception.core.internal.util.NestedLogger;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;

/** The actual client part of the {@link TurtleClient}. */
public class InternalClient extends NetworkAdapter {
    private final TurtleClientImpl client;

    private final String host;
    private final int port;

    /** The username to log in with */
    private final String login;
    /** The password used for en-/decryption */
    private final String pass;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private VirtualClient.Status status;

    public InternalClient(TurtleClientImpl client, String host, int port, String login, String pass) {
        super(new NestedLogger("Client#" + port, client.getLogger()));

        this.client = client;

        this.host = host;
        this.port = port;

        this.login = login;
        this.pass = pass;
    }

    @Override
    public void start() throws IOException {
        this.prepareExecutors();

        // create the underlying socket (the spicy part)
        this.socket = new Socket(host, port);
        this.status = VirtualClient.Status.CONNECTED;

        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        this.inputReader = new AsyncLoopThread(() -> status != VirtualClient.Status.DISCONNECTED, () -> {
            try {
                receive(in.readLine());
            } catch (IOException e) {
                client.getLogger().log(Level.WARNING, "Could not read input from client " + socket.getInetAddress(), e);
            }
        });

        // TODO: login
    }

    @Override
    public void stop() throws IOException {
        // notify server
        new VoidAction(this.client, Route.Login.QUIT).queue();

        this.stopInputReader();
        this.awaitExecutorShutdown();

        // close socket
        this.status = VirtualClient.Status.DISCONNECTED;
        this.in.close();
        this.out.close();
        this.socket.close();
    }

    /* - - - */

    /**
     * Called by {@link InternalClient#submitOutbound(ActionImpl)} when a message should be sent to a server.
     * <p>This method translates the {@link Message} into a String and encrypts it before passing it on to the
     * {@link InternalClient#out Socket OutputStream}.
     */
    void send(@NotNull Message msg) {
        String str = Message.parseToServer(msg);
        // TODO: encrypt
        out.write(str);
    }

    /**
     * Called by the {@link InternalClient#inputReader} when the server sent a message.
     * <p>This method decrypts the message and translates it into a {@link RemoteActionImpl} before passing it on to
     * {@link InternalClient#submitInbound(RemoteActionImpl)}.
     * @param msg The raw message from the server.
     */
    void receive(@NotNull String msg) {
        String str = msg; // TODO: decrypt
        Message message = Message.parseFromServer(msg);

        // attempt to find the initial request this message is a response to
        AnswerableAction<?> respondingTo = (message.callbackCode() > 0) ? callbackRegistrar.getAction(message.callbackCode()) : null;

        // parse the message into an action, so it can be processed
        RemoteActionImpl action = ActionParser.parseAction(client, message.content(), respondingTo);

        this.submitInbound(action);
    }

    /* - - - */

    @Override
    public @NotNull <T> CompletableFuture<T> submitOutbound(@NotNull ActionImpl<T> action) throws RejectedExecutionException, TimeoutException {
        if (this.isStopped())
            throw new RejectedExecutionException("The internal client has been stopped!");

        String content   = action.getRoute().getMessage();
        int callbackCode = (action instanceof AnswerableAction<T> aAction) ? callbackRegistrar.register(aAction) : 0;

        this.send(new Message(callbackCode, content));

        return this.outboundExecutor.register(action);
    }

    @Override
    public void submitInbound(@NotNull RemoteActionImpl action) {
        this.inboundExecutor.register(action);
    }

    @Override
    public boolean isStopped() {
        return socket.isClosed();
    }
}
