package de.turtle_exception.client.internal.data;

public class IllegalJsonException extends RuntimeException {
    public IllegalJsonException(String message) {
        super(message);
    }

    public IllegalJsonException(Throwable cause) {
        super(cause);
    }
}
