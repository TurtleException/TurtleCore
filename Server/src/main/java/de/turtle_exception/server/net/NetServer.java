package de.turtle_exception.server.net;

import com.google.common.collect.Sets;
import de.turtle_exception.client.internal.NetworkAdapter;
import de.turtle_exception.client.internal.net.Connection;
import de.turtle_exception.client.internal.net.Handshake;
import de.turtle_exception.client.internal.util.Worker;
import de.turtle_exception.server.TurtleServer;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
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

                try {
                    Handshake handshake  = new ServerHandshake(this);
                    Connection connection = new Connection(this, client, handshake, null);

                    this.clients.add(connection);
                } catch (IOException | LoginException e) {
                    getLogger().log(Level.WARNING, "Handshake failed.", e);
                }
            } catch (IOException e) {
                getLogger().log(Level.WARNING, "Could not establish requested connection.", e);
            }
        });
    }

    @Override
    public void onStop() throws IOException {
        this.listener.interrupt();
        this.listener = null;

        for (Connection connection : this.clients)
            connection.stop();
        this.clients.clear();

        this.socket.close();
    }
}
