package de.turtle_exception.client.internal.net;

import de.turtle_exception.client.internal.net.packets.HandshakePacket;
import de.turtle_exception.client.internal.util.version.Version;
import kotlin.NotImplementedError;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

class ClientHandshake extends Handshake {
    protected final String login;

    public ClientHandshake(@NotNull Version version, @NotNull String login) {
        super(version);
        this.login = login;
    }

    @Override
    public void handle(@NotNull HandshakePacket packet) {
        String msg = packet.getMessage();

        this.logger.log(Level.FINER, "Received message: " + msg);

        switch (msg) {
            case "LOGIN OK" -> this.done();
            case "VERSION"  -> this.sendMsg(packet, "VERSION " + version);
            case "LOGIN"    -> this.sendMsg(packet, "LOGIN " + login);
            default -> this.sendError(packet, "Unknown command", new NotImplementedError());
        }
    }
}
