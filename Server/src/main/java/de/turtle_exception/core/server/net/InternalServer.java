package de.turtle_exception.core.server.net;

import com.google.common.collect.Sets;
import de.turtle_exception.core.netcore.net.route.Routes;
import de.turtle_exception.core.netcore.util.AsyncLoopThread;
import de.turtle_exception.core.netcore.util.logging.NestedLogger;
import de.turtle_exception.core.server.TurtleServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.logging.Level;

/** The actual server part of the {@link TurtleServer}. */
public class InternalServer {
    private final TurtleServer server;
    private final int port;

    private final NestedLogger logger;

    private boolean online = false;

    private ServerSocket socket;
    Set<VirtualClient> clients;

    protected AsyncLoopThread receiver;

    public InternalServer(TurtleServer server, int port) {
        this.server = server;
        this.port = port;

        this.logger = new NestedLogger("Server#" + port, server.getLogger());
    }

    /**
     * Starts the server socket.
     * @throws IOException if an I/O error occurs when opening the socket.
     * @throws IllegalArgumentException if the port parameter is outside the specified range of valid port values,
     *                                  which is between 0 and 65535, inclusive.
     */
    public void start() throws IOException, IllegalArgumentException {
        this.clients = Sets.newConcurrentHashSet();

        // create the underlying socket (the spicy part)
        this.socket = new ServerSocket(port);

        this.receiver = new AsyncLoopThread(() -> online, () -> {
            try {
                Socket client = socket.accept();
                new LoginHandler(this, client);
            } catch (IOException e) {
                logger.log(Level.WARNING, "Could not establish requested connection.", e);
            }
        });
    }

    /**
     * Executes remaining Requests, closes all existing connections and stops the server.
     * @throws IOException if an I/O error occurs when closing the socket.
     */
    public void stop() throws IOException {
        // notify clients

        this.receiver.interrupt();
        this.receiver = null;

        // close socket
        this.online = false;
        this.socket.close();
    }

    /* - - - */

    @Nullable String getPass(@NotNull String login) {
        // TODO (DataService required)
    }

    synchronized boolean registerLogin(@NotNull String login) {
        // TODO
    }

    /* - - - */

    public boolean isStopped() {
        return !online;
    }

    public Set<VirtualClient> getClients() {
        return Set.copyOf(clients);
    }

    public @Nullable VirtualClient getClient(@NotNull String identifier) {
        if (clients == null) return null;
        for (VirtualClient client : clients)
            if (client.getIdentifier().equals(identifier))
                return client;
        return null;
    }

    public TurtleServer getServer() {
        return server;
    }

    public NestedLogger getLogger() {
        return logger;
    }
}
