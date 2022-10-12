package de.turtle_exception.server.ui.cli.commands;

import de.turtle_exception.server.ui.cli.Command;
import org.jetbrains.annotations.NotNull;

public class CommandLog extends Command {
    public CommandLog() {
        super("log", new String[]{"level"});
    }

    @Override
    public String execute(@NotNull String command, @NotNull String[] args) throws IllegalArgumentException {
        // TODO
    }

    @Override
    public String getUsage() {
        return "log level <level>";
    }
}
