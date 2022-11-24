package de.turtle_exception.server.cli;

@FunctionalInterface
public interface Command {
    void execute(String[] args) throws Exception;
}
