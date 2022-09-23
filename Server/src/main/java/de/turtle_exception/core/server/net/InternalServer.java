package de.turtle_exception.core.server.net;

import com.google.common.collect.Sets;
import de.turtle_exception.core.server.TurtleServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;

/** The actual server part of the {@link TurtleServer}. */
public class InternalServer extends NetworkAdapter {
    private final TurtleServerImpl server;
    private final int port;

    private boolean online = false;

    private ServerSocket socket;
    private Set<VirtualClient> clients;

    public InternalServer(TurtleServerImpl server, int port) {
        super(new NestedLogger("Server#" + port, server.getLogger()));

        this.server = server;
        this.port = port;
    }

    /**
     * Starts the server socket.
     * @throws IOException if an I/O error occurs when opening the socket.
     * @throws IllegalArgumentException if the port parameter is outside the specified range of valid port values,
     *                                  which is between 0 and 65535, inclusive.
     */
    @Override
    public void start() throws IOException, IllegalArgumentException {
        this.prepareExecutors();

        this.clients = Sets.newConcurrentHashSet();

        // create the underlying socket (the spicy part)
        this.socket = new ServerSocket(port);

        this.inputReader = new AsyncLoopThread(() -> online, () -> {
            try {
                Socket client = socket.accept();
                clients.add(new VirtualClient(InternalServer.this, client));
            } catch (IOException e) {
                logger.log(Level.WARNING, "Could not establish requested connection.", e);
            }
        });
    }

    /**
     * Executes remaining Requests, closes all existing connections and stops the server.
     * @throws IOException if an I/O error occurs when closing the socket.
     */
    @Override
    public void stop() throws IOException {
        // close client connections
        for (VirtualClient client : clients) {
            clients.remove(client);

            // notify client
            new VoidAction(this.server, Route.Login.QUIT, client)
                    .onSuccess(v -> {
                        // close connection
                        try {
                            client.closeSocket();
                        } catch (IOException e) {
                            logger.log(Level.WARNING, "Encountered an exception when attempting to close connection to client \"" + client.getIdentifier() + "\".", e);
                        }})
                    .queue();
        }

        this.stopInputReader();
        this.awaitExecutorShutdown();

        // close socket
        this.online = false;
        this.socket.close();
    }

    /* - - - */

    void receive(@NotNull Message msg) {
        // attempt to find the initial request this message is a response to
        AnswerableAction<?> respondingTo = (msg.callbackCode() > 0) ? callbackRegistrar.getAction(msg.callbackCode()) : null;

        // parse the message into an action, so it can be processed
        RemoteActionImpl action = ActionParser.parseAction(server, msg.client(), msg.content(), respondingTo);

        this.submitInbound(action);
    }

    /* - - - */

    @Override
    public @NotNull <T> CompletableFuture<T> submitOutbound(@NotNull ActionImpl<T> action) throws RejectedExecutionException, TimeoutException {
        if (this.isStopped())
            throw new RejectedExecutionException("The internal server has been stopped!");

        String content   = action.getRoute().getMessage();
        int callbackCode = (action instanceof AnswerableAction<T> aAction) ? callbackRegistrar.register(aAction) : 0;

        VirtualClient client = action.getTarget();

        // this should not happen
        if (client == null)
            throw new RejectedExecutionException("ActionImpl#getTarget may not be null on outbound server actions.");

        client.send(new Message(callbackCode, content, client));

        return this.outboundExecutor.register(action);
    }

    @Override
    public void submitInbound(@NotNull RemoteActionImpl action) {
        this.inboundExecutor.register(action);
    }

    @Override
    public boolean isStopped() {
        return !online;
    }

    /* - - - */

    public Set<VirtualClient> getClients() {
        return clients;
    }

    public @Nullable VirtualClient getClient(@NotNull String identifier) {
        if (clients == null) return null;
        for (VirtualClient client : clients)
            if (client.getIdentifier().equals(identifier))
                return client;
        return null;
    }
}
