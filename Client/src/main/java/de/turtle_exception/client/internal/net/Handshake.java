package de.turtle_exception.client.internal.net;

import de.turtle_exception.client.internal.net.packets.ErrorPacket;
import de.turtle_exception.client.internal.net.packets.HandshakePacket;
import de.turtle_exception.client.internal.net.packets.Packet;
import de.turtle_exception.client.internal.util.version.Version;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

public abstract class Handshake {
    protected final @NotNull Connection connection;
    protected final @NotNull Version version;

    protected Handshake(@NotNull Connection connection, @NotNull Version version) {
        this.connection = connection;
        this.version = version;
    }

    public void init() { }

    public void handle(@NotNull ErrorPacket error) {
        this.fail(error.getMessage(), error.getThrowable());
    }

    protected final void fail(@NotNull String msg, @Nullable Throwable t) {
        connection.getLogger().log(Level.WARNING, "Handshake failed: " + msg, t);
        connection.stop(false);
    }

    public abstract void handle(@NotNull HandshakePacket packet);

    protected final void sendError(@NotNull Packet respondingTo, @NotNull String msg, @Nullable Throwable t) {
        this.connection.send(
                new ErrorPacket(
                        connection.getAdapter().getClient().getDefaultTimeoutOutbound(),
                        connection,
                        respondingTo.getResponseCode(),
                        msg,
                        t
                ).compile()
        );
    }

    protected final void sendMsg(@NotNull Packet respondingTo, @NotNull String msg) {
        this.connection.send(
                new HandshakePacket(
                        connection.getAdapter().getClient().getDefaultTimeoutOutbound(),
                        connection,
                        respondingTo.getResponseCode(),
                        msg
                ).compile()
        );
    }

    protected final void done() {
        this.connection.status = Connection.Status.CONNECTED;
    }
}
