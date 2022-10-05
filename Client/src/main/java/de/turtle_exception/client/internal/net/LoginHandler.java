package de.turtle_exception.client.internal.net;

import de.turtle_exception.core.net.NetworkAdapter;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

class LoginHandler extends Thread {
    private final PrintWriter    out;
    private final BufferedReader in;

    private final String version;
    private final String login;

    private final AtomicBoolean done = new AtomicBoolean(false);
    private final AtomicReference<LoginException> error = new AtomicReference<>();

    public LoginHandler(@NotNull PrintWriter out, @NotNull BufferedReader in, @NotNull String version, @NotNull String login) {
        super();

        this.out = out;
        this.in  = in;

        this.version = version;
        this.login   = login;

        this.setDaemon(true);
        this.start();
    }

    @Override
    public void run() {
        try {
            String buffer;
            while ((buffer = in.readLine()) != null) {
                if (buffer.equals(NetworkAdapter.LOGGED_IN))
                    break;

                if (buffer.startsWith("ERROR")) {
                    String err = buffer.substring("ERROR ".length());
                    throw new LoginException(switch (err) {
                        case "VERSION ILLEGAL"      -> "Illegal Version";
                        case "VERSION INCOMPATIBLE" -> "Incompatible version.";
                        case "LOGIN ILLEGAL"        -> "Invalid login credentials.";
                        case "LOGIN TAKEN"          -> "Already logged in.";
                        default -> "Unknown error";
                    });
                }

                if (buffer.equals("VERSION"))
                    out.println("VERSION " + version);

                if (buffer.equals("LOGIN"))
                    out.println("LOGIN " + login);
            }

            this.done.set(true);
        } catch (LoginException e) {
            this.error.set(e);
        } catch (IOException e) {
            this.error.set(new LoginException("Unexpected IO error: " + e.getMessage()));
        } catch (IndexOutOfBoundsException e) {
            this.error.set(new LoginException("Unexpected message substring error: " + e.getMessage()));
        }
    }

    public void await(long timeout, @NotNull TimeUnit unit) throws LoginException {
        final long deadline = unit.toMillis(timeout);

        while (System.currentTimeMillis() < deadline) {
            if (error.get() != null)
                throw error.get();

            if (done.get())
                return;
        }
        throw new LoginException("Timed out while attempting to log in.");
    }
}
