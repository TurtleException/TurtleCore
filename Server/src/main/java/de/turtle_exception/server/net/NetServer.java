package de.turtle_exception.server.net;

import com.google.common.collect.Sets;
import de.turtle_exception.client.internal.NetworkAdapter;
import de.turtle_exception.client.internal.data.Data;
import de.turtle_exception.client.internal.net.Connection;
import de.turtle_exception.client.internal.net.Handshake;
import de.turtle_exception.client.internal.net.NetworkProvider;
import de.turtle_exception.client.internal.net.packets.DataPacket;
import de.turtle_exception.client.internal.net.packets.ErrorPacket;
import de.turtle_exception.client.internal.net.packets.HeartbeatPacket;
import de.turtle_exception.client.internal.net.packets.Packet;
import de.turtle_exception.client.internal.util.Worker;
import de.turtle_exception.server.TurtleServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class NetServer extends NetworkAdapter {
    private final TurtleServer server;
    private final int port;

    private Set<Connection> clients;
    private ServerSocket socket;
    private Worker heartbeats;
    private Worker listener;

    public NetServer(@NotNull TurtleServer server, int port) {
        this.server = server;
        this.port = port;
    }

    @Override
    public void onStart() throws IOException {
        this.clients = Sets.newConcurrentHashSet();

        this.socket = new ServerSocket(port);

        this.heartbeats = new Worker(() -> status == Status.CONNECTED, () -> {
            this.clients.forEach(connection -> {
                connection.send(
                        new HeartbeatPacket(server.getClient().getDefaultTimeoutOutbound(), connection).compile()
                );
            });
        }, 10, TimeUnit.SECONDS);

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
            connection.stop(true);
        this.clients.clear();

        this.heartbeats.interrupt();
        this.heartbeats = null;

        this.socket.close();
    }

    @Override
    public void handleDataRequest(@NotNull DataPacket packet) {
        // this would create a loop
        if (getClient().getProvider() instanceof NetworkProvider)
            throw new AssertionError("Unsupported provider");

        switch (packet.getData().method()) {
            case DELETE -> this.handleDelete(packet);
            case GET    -> this.handleGet(packet);
            case PUT    -> this.handlePut(packet);
            case PATCH  -> this.handlePatch(packet);
            case UPDATE, REMOVE -> throw new UnsupportedOperationException();
            default -> throw new AssertionError();
        }
    }

    /* - - - */

    // TODO: use the server cache first

    private void handleDelete(@NotNull DataPacket packet) {
        Class<?> type = packet.getData().type();
        long id = packet.getData().id();

        // TODO
    }

    private void handleGet(@NotNull DataPacket packet) {
        Class<?> type = packet.getData().type();
        long id = packet.getData().id();

        // TODO
    }

    private void handlePut(@NotNull DataPacket packet) {
        Class<?> type = packet.getData().type();
        long id = packet.getData().id();

        // TODO
    }

    private void handlePatch(@NotNull DataPacket packet) {
        Class<?> type = packet.getData().type();
        long id = packet.getData().id();

        // TODO
    }

    /* - HELPER METHODS - */

    private void respond(@NotNull Packet packet, @NotNull Data data) {
        packet.getConnection().send(
                new DataPacket(defaultDeadline(), packet.getConnection(), packet.getResponseCode(), data), true
        );
    }

    private void respond(@NotNull Packet packet, @NotNull String error, @Nullable Throwable t) {
        packet.getConnection().send(
                new ErrorPacket(defaultDeadline(), packet.getConnection(), packet.getResponseCode(), error, t), true
        );
    }

    private long defaultDeadline() {
        return System.currentTimeMillis() + getClient().getDefaultTimeoutOutbound();
    }
}
