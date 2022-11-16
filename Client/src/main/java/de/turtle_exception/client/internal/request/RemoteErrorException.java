package de.turtle_exception.client.internal.request;

import org.jetbrains.annotations.NotNull;

public class RemoteErrorException extends Exception {
    public RemoteErrorException(String msg) {
        super(msg);
    }

    public RemoteErrorException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public RemoteErrorException(@NotNull ErrorResponse errorResponse) {
        this(errorResponse.packet().getMessage(), errorResponse.packet().getThrowable());
    }
}
