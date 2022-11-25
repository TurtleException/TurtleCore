package de.turtle_exception.server.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import de.turtle_exception.client.internal.util.logging.NestedLogger;
import de.turtle_exception.server.TurtleServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;

public class LoginHandler {
    private final @NotNull TurtleServer server;
    private final @NotNull NestedLogger logger;

    private final Object lock = new Object();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private final File loginFile;

    public LoginHandler(@NotNull TurtleServer server) {
        this.server = server;
        this.logger = new NestedLogger("LoginHandler", server.getLogger());

        this.loginFile = new File(TurtleServer.DIR, "meta" + File.separator + "login.json");
    }

    /* - - - */

    public @Nullable String checkLogin(@NotNull String login) throws LoginException {
        synchronized (lock) {
            try {
                this.makefile();

                JsonObject json = gson.fromJson(new FileReader(loginFile), JsonObject.class);

                String pass = json.get(login).getAsString();

                if (pass == null)
                    throw new LoginException("Unknown login or pass");

                this.logger.log(Level.INFO, "Permitted check for login '" + login + "'");
                return pass;
            } catch (IOException e) {
                this.logger.log(Level.WARNING, "Internal IO error for checkLogin request '" + login + "'", e);
                throw new LoginException("Internal IO error");
            } catch (ClassCastException | IllegalStateException e) {
                this.logger.log(Level.FINE, "JSON error for checkLogin request '" + login + "'", e);
                return null;
            } catch (LoginException e) {
                // we don't need the stacktrace in the log
                this.logger.log(Level.INFO, "Failed check for login '" + login + "': " + e);
                throw e;
            } catch (Throwable t) {
                this.logger.log(Level.WARNING, "Unknown internal error for login request '" + login + "'", t);
                throw new LoginException("Unknown internal error");
            }
        }
    }

    public @NotNull String addLogin(@NotNull String login, @NotNull String pass) {
        synchronized (lock) {
            try {
                this.makefile();

                try {
                    if (this.checkLogin(login) != null)
                        return "Login already exists!";
                } catch (LoginException ignored) { }

                JsonObject json = gson.fromJson(new FileReader(loginFile), JsonObject.class);
                json.addProperty(login, pass);

                try (FileWriter writer = new FileWriter(loginFile, false)) {
                    writer.write(gson.toJson(json));
                }

                this.logger.log(Level.INFO, "Created new login '" + login + "'");

                return "Login '" + login + "' created.";
            } catch (IOException e) {
                this.logger.log(Level.WARNING, "Internal IO error for addLogin request '" + login + "'", e);
                return "Internal error. Check logs for more info.";
            }
        }
    }

    public @NotNull String delLogin(@NotNull String login) {
        synchronized (lock) {
            try {
                this.makefile();

                JsonObject json = gson.fromJson(new FileReader(loginFile), JsonObject.class);

                if (!json.has(login))
                    return "Login '" + login + "' does not exist.";

                json.remove(login);

                try (FileWriter writer = new FileWriter(loginFile, false)) {
                    writer.write(gson.toJson(json));
                }

                this.logger.log(Level.INFO, "Deleted login '" + login + "'");

                return "Login '" + login + "' deleted.";
            } catch (IOException e) {
                this.logger.log(Level.WARNING, "Internal IO error for delLogin request '" + login + "'", e);
                return "Internal error. Check logs for more info.";
            }
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void makefile() throws IOException {
        synchronized (lock) {
            loginFile.getParentFile().mkdir();
            loginFile.createNewFile();
        }
    }

    /* - - - */

    public @NotNull TurtleServer getServer() {
        return this.server;
    }
}
