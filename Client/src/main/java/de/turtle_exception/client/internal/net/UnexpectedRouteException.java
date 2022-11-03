package de.turtle_exception.client.internal.net;

import de.turtle_exception.client.internal.net.route.Route;

/** Thrown to indicate that a provided {@link Route} on an incoming request is unexpected and cannot be handled. */
public class UnexpectedRouteException extends RuntimeException {
    /** Constructs a new UnexpectedRouteException with the specified detail message. */
    public UnexpectedRouteException(String message) {
        super(message);
    }
}
