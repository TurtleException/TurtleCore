package de.turtle_exception.server.ui.cli;

import org.jetbrains.annotations.NotNull;

public abstract class Command {
    protected final @NotNull String command;
    protected final @NotNull String[] params;

    public Command(@NotNull String command, @NotNull String[] params) {
        this.command = command.toLowerCase();
        this.params = params;
    }

    public Command(@NotNull String command) {
        this.command = command.toLowerCase();
        this.params = new String[]{};
    }

    public final String handle(String command) throws IllegalArgumentException {
        if (command == null)
            throw new IllegalArgumentException("Illegal command: null");

        String[] args;
        try {
            args = command.substring(this.command.length() + 1).split(" ");
        } catch (IndexOutOfBoundsException e) {
            args = new String[]{};
        }

        if (command.toLowerCase().startsWith(this.command))
            throw new IllegalArgumentException("Illegal command: " + command);

        if (args.length != params.length)
            throw new IllegalArgumentException("Illegal amount of arguments. (" + params.length + " required, " + args.length + " provided)");

        return this.execute(command, args);
    }

    public abstract String execute(@NotNull String command, @NotNull String[] args) throws IllegalArgumentException;

    public abstract String getUsage();

    public @NotNull String getCommand() {
        return command;
    }
}
