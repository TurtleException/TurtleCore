package de.turtle_exception.server.net;

import de.turtle_exception.client.internal.net.Handshake;
import de.turtle_exception.client.internal.util.version.IllegalVersionException;
import de.turtle_exception.client.internal.util.version.Version;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.util.concurrent.atomic.AtomicReference;

class ServerHandshake extends Handshake {
    private final AtomicReference<Version> clientVersion = new AtomicReference<>(null);
    private final AtomicReference<String>  clientLogin   = new AtomicReference<>(null);

    private final NetServer server;

    public ServerHandshake(@NotNull NetServer server) {
        super(server.getClient().getVersion());
        this.server = server;
    }

    @Override
    public boolean onInput(@NotNull String str) throws LoginException {
        if (str.startsWith("ERROR"))
            throw new LoginException("Request refused by client: " + str.substring("ERROR ".length()));

        if (clientVersion.get() == null) {
            if (!str.startsWith("VERSION")) {
                this.out.println("VERSION");
                return false;
            }

            String rawVersion = str.substring("VERSION ".length());
            try {
                Version clVersion = Version.parse(rawVersion);

                if (version.getMajor() != clVersion.getMajor())
                    throw new IllegalVersionException();

                if (version.getMinor() != clVersion.getMinor())
                    throw new IllegalVersionException();

                clientVersion.set(clVersion);
                return false;
            } catch (IllegalVersionException e) {
                this.out.println("ERROR VERSION ILLEGAL");
                throw new LoginException("Illegal version: " + rawVersion);
            }
        }

        if (clientLogin.get() == null) {
            if (!str.startsWith("LOGIN")) {
                this.out.println("LOGIN");
                return false;
            }

            String rawLogin = str.substring("LOGIN ".length());
            // TODO: check login

            clientLogin.set(rawLogin);
            this.out.println("LOGIN OK");
            return true;
        }

        this.out.println("ERROR UNKNOWN");
        throw new LoginException("Unknown input: " + str);
    }

    @Override
    public void onRun() {
        this.out.println("VERSION");
    }
}
