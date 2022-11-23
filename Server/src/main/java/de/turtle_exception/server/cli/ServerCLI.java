package de.turtle_exception.server.cli;

import com.google.common.io.Resources;
import de.turtle_exception.client.internal.util.StringUtil;
import de.turtle_exception.server.TurtleServer;
import de.turtle_exception.server.util.StatusView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class ServerCLI {
    private static final URL ALIAS_URL = Resources.getResource("help" + File.separator + ".aliases");

    private final @NotNull TurtleServer server;
    private final @NotNull PrintStream  out;

    private boolean verboseMode = false;

    public ServerCLI(@NotNull TurtleServer server) {
        this.server = server;
        this.out = new PrintStream(System.out);
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
                List<String> lines;
                if (args.length == 0)
                    lines = Resources.readLines(Resources.getResource("help.txt"), Charset.defaultCharset());
                else if (args.length == 1)
                    lines = retrieveHelp(args[0]);
                else {
                    logError("Too many arguments!", null);
                    return;
                }
                for (String line : lines)
                    out.println(line);
            });
        else if (StringUtil.startsWith(low, "level", "log-level"))
            this.execute(input, args -> {
                if (args.length == 0) {
                    out.println("Current log-level is " + server.getLogger().getLevel().toString());
                } else if (args.length == 1) {
                    Level level = Level.parse(args[0]);
                    server.getLogger().setLevel(level);
                    out.println("Leg-level set to " + level.toString() + ".");
                } else {
                    logError("Too many arguments!", null);
                }
            });
        else
            out.println("Unknown command. Use 'help' for a list of commands.");
    }

    private void execute(@NotNull String input, @NotNull Command cmd) {
        final String[] tokens = input.split(" ");
        final String[] args   = new String[tokens.length - 1];

        if (args.length < 1)
            System.arraycopy(tokens, 1, args, 0, args.length);

        this.out.println("Executing command \"" + tokens[0] + "\" with args " + Arrays.toString(args));

        try {
            cmd.execute(args);
        } catch (Exception e) {
            this.logError("Failed to execute command", e);
        }
    }

    private void logError(@NotNull String msg, @Nullable Exception e) {
        this.out.println(msg);
        if (verboseMode && e != null)
            e.printStackTrace(this.out);
        if (!verboseMode && e != null)
            this.out.println("Use command 'verbose' to enable exception logging.");
    }

    private List<String> retrieveHelp(@NotNull String command) throws IOException {
        String low     = command.toLowerCase();
        String pointer = "";

        for (String alias : Resources.readLines(ALIAS_URL, Charset.defaultCharset())) {
            if (alias.startsWith(low)) {
                pointer = alias.substring(alias.indexOf('@') + 1);
                break;
            }
        }

        if (pointer.equals(".aliases"))
            throw new AssertionError();

        if (pointer.isEmpty())
            return List.of("Unknown command. Use 'help' for a list of commands");

        URL url = Resources.getResource("help" + File.separator + pointer + ".txt");
        return Resources.readLines(url, Charset.defaultCharset());
    }
}
