package de.turtle_exception.server.ui.cli;

import de.turtle_exception.server.ui.cli.commands.CommandLog;
import de.turtle_exception.server.ui.cli.commands.CommandQuit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class Receiver {
    private final InputStream in;
    private final PrintStream out;

    private final Scanner scanner;

    public Receiver(@NotNull InputStream in, @NotNull PrintStream out) {
        this.in = in;
        this.out = out;

        this.scanner = new Scanner(in);
    }

    public void receive() {
        if (!scanner.hasNextLine()) return;
        this.handle(scanner.nextLine());
    }

    private void handle(String str) {
        Command cmd = getCommand(str);

        if (cmd == null) {
            out.println("Unknown command: " + str);
            return;
        }

        try {
            cmd.handle(str);
        } catch (Exception e) {
            // TODO
        }
    }

    /* - - - */

    private static final Command[] COMMANDS = {
            new CommandLog(),
            new CommandQuit()
    };

    private static @Nullable Command getCommand(String str) {
        if (str == null) return null;

        String strLow = str.toLowerCase();

        for (Command cmd : COMMANDS)
            if (strLow.startsWith(cmd.getCommand()))
                return cmd;

        return null;
    }
}
