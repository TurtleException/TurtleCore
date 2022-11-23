package de.turtle_exception.server.cli;

import de.turtle_exception.client.internal.util.StringUtil;
import de.turtle_exception.server.TurtleServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.logging.Level;

public class ServerCLI {
    private final @NotNull TurtleServer server;

    private boolean logExceptions = false;

    public ServerCLI(@NotNull TurtleServer server) {
        this.server = server;
    }

    public void handle(final String input) {
        if (input == null)   return;
        if (input.isBlank()) return;

        final String low = input.toLowerCase();

        // TODO: commands
    }

    private void execute(@NotNull String input, @NotNull Command cmd) {
        final String[] tokens = input.split(" ");
        final String[] args   = new String[tokens.length - 1];

        if (args.length < 1)
            System.arraycopy(tokens, 1, args, 0, args.length);

        this.server.getLogger().log(Level.FINER, "Executing command \"" + tokens[0] + "\" with args " + Arrays.toString(args));

        try {
            cmd.execute(args);
        } catch (Exception e) {
            this.server.getLogger().log(Level.WARNING, "");
        }
    }

    private void logError(@NotNull String msg, @Nullable Exception e) {
        if (logExceptions)
            this.server.getLogger().log(Level.WARNING, msg, e);
        else
            this.server.getLogger().log(Level.WARNING, msg + " Use command 'verbose' to enable exception logging.");
    }
}
