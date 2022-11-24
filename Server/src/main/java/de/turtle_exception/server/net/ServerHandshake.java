package de.turtle_exception.server.net;

import de.turtle_exception.client.internal.net.Handshake;
import de.turtle_exception.client.internal.net.packets.HandshakePacket;
import de.turtle_exception.client.internal.util.version.IllegalVersionException;
import de.turtle_exception.client.internal.util.version.Version;
import kotlin.NotImplementedError;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;

class ServerHandshake extends Handshake {
    private final AtomicReference<Version> clientVersion = new AtomicReference<>(null);
    private final AtomicReference<String>  clientLogin   = new AtomicReference<>(null);

    private final NetServer server;

    public ServerHandshake(@NotNull NetServer server) {
        super(server.getClient().getVersion());
        this.server = server;
    }

    @Override
    public void init() {
        // initial request (version)
        this.connection.send(
                new HandshakePacket(
                        server.getClient().getDefaultTimeoutOutbound(),
                        connection,
                        "VERSION"
                ), false
        );
    }

    @Override
    public void handle(@NotNull HandshakePacket packet) {
        String msg = packet.getMessage();

        this.logger.log(Level.FINE, "Received message: " + msg);

        if (msg.startsWith("VERSION")) {
            String raw = msg.substring("VERSION ".length());

            try {
                Version clVersion = Version.parse(raw);

                if (version.getMajor() != clVersion.getMajor())
                    throw new IllegalVersionException("Major version does not match");

                if (version.getMinor() != clVersion.getMinor())
                    throw new IllegalVersionException("Minor version does not match");

                clientVersion.set(clVersion);

                this.sendMsg(packet, "LOGIN");
                return;
            } catch (IllegalVersionException e) {
                this.sendError(packet, "Illegal version", e);
                this.fail("Illegal version", e);
            }
        }

        if (msg.startsWith("LOGIN")) {
            String login = msg.substring("LOGIN ".length());
            String pass = null;

            try {
                pass = server.getServer().getLoginHandler().checkLogin(login);
            } catch (LoginException e) {
                this.sendError(packet, "Login error", e);
            }

            if (pass == null) {
                this.sendError(packet, "Illegal login", null);
                return;
            }

            clientLogin.set(login);
            connection.setPass(pass);

            this.done();
            this.sendMsg(packet, "LOGIN OK");
            return;
        }

        this.sendError(packet, "Unknown command", new NotImplementedError());
    }
}
