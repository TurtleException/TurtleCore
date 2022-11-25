package de.turtle_exception.client.internal.util.logging;

import org.jetbrains.annotations.NotNull;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

public class ConsoleHandler extends StreamHandler {
    public ConsoleHandler(@NotNull Formatter formatter) {
        super(System.err, formatter);
    }

    @Override
    public synchronized void publish(LogRecord record) {
        super.publish(record);
        flush();
    }
}
