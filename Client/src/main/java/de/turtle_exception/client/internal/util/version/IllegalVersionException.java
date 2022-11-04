package de.turtle_exception.client.internal.util.version;

public class IllegalVersionException extends RuntimeException {
    IllegalVersionException(String raw, Throwable cause) {
        super("'" + raw + "' could not be converted to Version", cause);
    }

    public IllegalVersionException() {
        super();
    }

    public IllegalVersionException(String message) {
        super(message);
    }
}