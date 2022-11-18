package de.turtle_exception.client.internal.net;

import de.turtle_exception.client.internal.net.packets.ErrorPacket;
import de.turtle_exception.client.internal.net.packets.HandshakePacket;
import de.turtle_exception.client.internal.net.packets.Packet;
import de.turtle_exception.client.internal.util.logging.NestedLogger;
import de.turtle_exception.client.internal.util.version.Version;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

public abstract class Handshake {
    protected final @NotNull Version version;
    protected Connection connection;
    protected NestedLogger logger;

    protected Handshake(@NotNull Version version) {
        this.version = version;
    }

    final void setConnection(@NotNull Connection connection) {
        this.connection = connection;
        this.logger = new NestedLogger("Handshake", connection.getLogger());
        this.logger.log(Level.FINER, "Connection set!");
    }

    public void init() { }

    public void handle(@NotNull ErrorPacket error) {
        this.fail(error.getMessage(), error.getThrowable());
    }

    protected final void fail(@NotNull String msg, @Nullable Throwable t) {
        this.logger.log(Level.WARNING, "Handshake failed: " + msg, t);
        connection.stop(false);
    }

    public abstract void handle(@NotNull HandshakePacket packet);

    protected final void sendError(@NotNull Packet respondingTo, @NotNull String msg, @Nullable Throwable t) {
        this.logger.log(Level.WARNING, "Sending error: " + msg, t);
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
        this.logger.log(Level.FINE, "Sending message: " + msg);
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
        this.logger.log(Level.INFO, "Handshake done!");
    }
}
