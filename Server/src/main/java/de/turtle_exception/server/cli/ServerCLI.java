package de.turtle_exception.server.cli;

import de.turtle_exception.server.TurtleServer;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class ServerCLI {
    private final @NotNull TurtleServer server;

    public ServerCLI(@NotNull TurtleServer server) {
        this.server = server;
    }

    public void handle(final String input) {
        if (input == null) return;

        final String low = input.toLowerCase();

        // TODO: commands
    }
}
