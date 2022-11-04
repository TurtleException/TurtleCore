package de.turtle_exception.server.net;

import com.google.common.collect.Sets;
import de.turtle_exception.client.internal.net.Connection;
import de.turtle_exception.client.internal.net.NetworkAdapter;
import de.turtle_exception.client.internal.util.Worker;
import de.turtle_exception.server.TurtleServer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.logging.Level;

public class NetServer extends NetworkAdapter {
    private final TurtleServer server;
    private final int port;

    private Set<Connection> clients;
    private ServerSocket socket;
    private Worker listener;

    public NetServer(@NotNull TurtleServer server, int port) {
        this.server = server;
        this.port = port;
    }

    @Override
    public void onStart() throws IOException {
        this.clients = Sets.newConcurrentHashSet();

        this.socket = new ServerSocket(port);

        this.listener = new Worker(() -> status == Status.CONNECTED, () -> {
            try {
                Socket client = socket.accept();
                // TODO: cache client & initiate handshake
            } catch (IOException e) {
                getLogger().log(Level.WARNING, "Could not establish requested connection.", e);
            }
        });
    }

    @Override
    public void onStop() throws IOException {
        // TODO: close connections

        this.listener = null;
    }
}
