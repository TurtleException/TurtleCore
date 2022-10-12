package de.turtle_exception.server.ui.cli.commands;

import de.turtle_exception.server.ui.cli.Command;
import org.jetbrains.annotations.NotNull;

public class CommandQuit extends Command {
    public CommandQuit() {
        super("quit");
    }

    @Override
    public String execute(@NotNull String command, @NotNull String[] args) {
        // TODO
    }

    @Override
    public String getUsage() {
        return "quit";
    }
}
