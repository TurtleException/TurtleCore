package de.turtle_exception.server.cli;

import com.google.common.io.Resources;
import de.turtle_exception.client.internal.util.StringUtil;
import de.turtle_exception.server.TurtleServer;
import de.turtle_exception.server.util.StatusView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.logging.Level;

public class ServerCLI {
    private final @NotNull TurtleServer server;

    private boolean verboseMode = false;

    public ServerCLI(@NotNull TurtleServer server) {
        this.server = server;
    }

    public void handle(final String input) {
        if (input == null)   return;
        if (input.isBlank()) return;

        final String low = input.toLowerCase();


        if (StringUtil.startsWith(low, "exit", "shutdown", "stop", "quit"))
            this.execute(input, args -> server.getStatus().set(StatusView.STOPPING));
        else if (StringUtil.startsWith(low, "verbose"))
            this.execute(input, args -> verboseMode = !verboseMode);
        else if (StringUtil.startsWith(low, "help", "commands"))
            this.execute(input, args -> {
                URL url;
                if (args.length == 0)
                    url = Resources.getResource("help.txt");
                else if (args.length == 1)
                    url = Resources.getResource("help" + File.separator + args[0] + ".txt");
                else {
                    logError("Too many arguments!", null);
                    return;
                }
                for (String line : Resources.readLines(url, Charset.defaultCharset()))
                    this.server.getLogger().log(Level.ALL, line);
            });
        else if (StringUtil.startsWith(low, "level", "log-level"))
            this.execute(input, args -> {
                if (args.length == 0) {
                    server.getLogger().log(Level.ALL, "Current log-level is " + server.getLogger().getLevel().toString());
                } else if (args.length == 1) {
                    Level level = Level.parse(args[0]);
                    server.getLogger().setLevel(level);
                    server.getLogger().log(Level.ALL, "Leg-level set to " + level.toString() + ".");
                } else {
                    logError("Too many arguments!", null);
                }
            });
        else
            server.getLogger().log(Level.ALL, "Unknown command. Use 'help' for a list of commands.");
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
            this.logError("Failed to execute command", e);
        }
    }

    private void logError(@NotNull String msg, @Nullable Exception e) {
        if (verboseMode || e == null)
            this.server.getLogger().log(Level.WARNING, msg, e);
        else
            this.server.getLogger().log(Level.WARNING, msg + " Use command 'verbose' to enable exception logging.");
    }
}
