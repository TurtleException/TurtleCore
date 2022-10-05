package de.turtle_exception.server.data;

/**
 * Thrown to indicate that a call to a {@link DataService} has failed. This can have multiple reasons:
 * <ul>
 *     <li> The request has been sent with illegal parameters.
 *     <li> The requested data does not exist.
 *     <li> The underlying database could not be reached.
 *     <li> An exception occurred when attempting to parse data.
 * </ul>
 * Please see {@link DataAccessException#getMessage()} and/or {@link DataAccessException#getCause()} for further information.
 * <p> This is an implementation of {@link Exception} rather than {@link RuntimeException} to make sure any
 * implementation of a {@link DataService} can safely throw exceptions that may occur and the calling method is
 * responsible to handle these problems.
 */
public class DataAccessException extends Exception {
    /**
     * Constructs a new exception with a causing {@link Throwable}. This constructor is useful as a simple wrapper to
     * simplify exception handling in a {@link DataService} implementation.
     * @param cause the causing Throwable.
     */
    public DataAccessException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a {@code DataAccessException} with the specified detail message.
     * @param message the detail message.
     */
    public DataAccessException(String message) {
        super(message);
    }
}
