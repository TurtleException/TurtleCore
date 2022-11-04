package de.turtle_exception.client.internal.net;

import de.turtle_exception.client.internal.util.version.Version;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;

class ClientHandshake extends Handshake {
    protected final String login;

    public ClientHandshake(@NotNull Version version, @NotNull String login) {
        super(version);
        this.login = login;
    }

    @Override
    public boolean onInput(@NotNull String str) throws LoginException {
        if (str.equals("LOGIN OK")) return true;

        if (str.startsWith("ERROR")) {
            String err = str.substring("ERROR ".length());
            throw new LoginException(switch (err) {
                case "VERSION ILLEGAL"      -> "Illegal Version";
                case "VERSION INCOMPATIBLE" -> "Incompatible version.";
                case "LOGIN ILLEGAL"        -> "Invalid login credentials.";
                case "LOGIN TAKEN"          -> "Already logged in.";
                default -> "Unknown error";
            });
        }

        if (str.equals("VERSION")) {
            out.println("VERSION " + version);
            return false;
        }

        if (str.equals("LOGIN")) {
            out.println("LOGIN " + login);
            return false;
        }

        out.println("ERROR UNKNOWN");
        return false;
    }
}
