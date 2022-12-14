package de.turtle_exception.server.cli;

import com.google.common.io.Resources;
import de.turtle_exception.client.internal.util.Checks;
import de.turtle_exception.client.internal.util.StringUtil;
import de.turtle_exception.server.Main;
import de.turtle_exception.server.TurtleServer;
import de.turtle_exception.server.util.StatusView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class ServerCLI {
    @SuppressWarnings("ConstantConditions")
    private static final @NotNull URL ALIAS_URL = Main.class.getClassLoader().getResource("help/.aliases.txt");
    static {
        Checks.nonNull(ALIAS_URL, "ALIAS_URL");
    }

    private final @NotNull TurtleServer server;
    private final @NotNull PrintStream  out;

    private boolean verboseMode = false;

    public ServerCLI(@NotNull TurtleServer server) {
        this.server = server;
        this.out = new PrintStream(System.out);

        this.verboseMode = Boolean.parseBoolean(server.getConfig().getProperty("verboseMode", "false"));
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
                out.println();
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
        else if (StringUtil.startsWith(low, "invalidate-caches"))
            this.execute(input, args -> {
                if (args.length == 0) {
                    out.println("Too few arguments! Append [true|false] to indicate refill.");
                    return;
                }
                server.getClient().invalidateCaches(Boolean.parseBoolean(args[0])).queue(v -> {
                    out.println("Caches invalidated!");
                });
            });
        else if (StringUtil.startsWith(low, "adduser", "addlogin"))
            this.execute(input, args -> {
                if (args.length < 2) {
                    out.println("Too few arguments! Please provide <login> and <pass>");
                    return;
                }
                String login = args[1];
                out.print("\b".repeat(login.length() + 1));
                out.println("*".repeat(login.length()));
                out.println(server.getLoginHandler().addLogin(args[0], login));
            });
        else if (StringUtil.startsWith(low, "deluser", "dellogin"))
            this.execute(input, args -> {
                if (args.length < 1) {
                    out.println("Too few arguments! Please provide <login>");
                    return;
                }
                out.println(server.getLoginHandler().delLogin(args[0]));
            });
        else if (StringUtil.startsWith(low, "listuser", "listlogin", "listusers", "listlogins"))
            this.execute(input, args -> {
                List<String> lines = server.getLoginHandler().getLogins();
                out.println("All users:  " + StringUtil.join(",  ", lines));
            });
        else
            out.println("Unknown command. Use 'help' for a list of commands.");
    }

    private void execute(@NotNull String input, @NotNull Command cmd) {
        final String[] tokens = input.split(" ");
        final String[] args   = new String[tokens.length - 1];

        if (args.length > 0)
            System.arraycopy(tokens, 1, args, 0, args.length);

        server.getLogger().log(Level.FINE, "Executing command \"" + tokens[0] + "\" with args " + Arrays.toString(args));

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

        URL url = Resources.getResource("help/" + pointer + ".txt");
        return Resources.readLines(url, Charset.defaultCharset());
    }
}
