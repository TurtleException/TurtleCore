package de.turtle_exception.core.core.net.route;

import org.jetbrains.annotations.NotNull;

/**
 * A route method provides first information on how to process an incoming request.
 * <p> While these methods are partially based on the way HTTP request methods work they are not identical to them and
 * often differ in functionality. Please review the documentation before assuming similarities!
 */
public enum Method {
    /**
     * Contains information regarding the connection and / or communication between client and server. As example for
     * this would be the request to close the connection.
     * <p><b>Direction: </b> Client <-> Server
     * <p><b>Terminating: </b> No
     */
    META("META"),

    /**
     * Requests to delete the data specified by a provided identifier.
     * <p><b>Direction: </b> Client -> Server
     * <p><b>Terminating: </b> No
     */
    DELETE("DELETE"),

    /**
     * Requests data specified by a provided identifier. A GET request should never modify data.
     * <p><b>Direction: </b> Client -> Server
     * <p><b>Terminating: </b> No
     */
    GET("GET"),

    // TODO: need this?
    /**
     * Essentially a GET request but without actually sending data. This can be used to check whether data is available.
     * <p><b>Direction: </b> Client -> Server
     * <p><b>Terminating: </b> No
     */
    HEAD("HEAD"),

    /**
     * Requests to write data. This can either mean that existing data should be updated or the provided data should
     * simply be saved as new.
     * <p><b>Direction: </b> Client -> Server
     * <p><b>Terminating: </b> No
     */
    PUT("PUT"),

    /**
     * Essentially a PUT request but only with partial data. While PUT requests are required to send entire objects and
     * will fail if these objects are not complete, a PATCH request may provide only part of the data. This can be used
     * to request partial modifications without running into concurrency issues.
     * <p> While a server could technically send these types of requests to a client this method is strictly reserved
     * for client-to-server communication to ensure cache validity on the client-side.
     * <p><b>Direction: </b> Client -> Server
     * <p><b>Terminating: </b> No
     */
    PATCH("PATCH"),

    /**
     * Provides the user with data that should be updated in the client cache.
     * <p><b>Direction: </b> Client <- Server
     * <p><b>Terminating: </b> Yes
     */
    UPDATE("UPDATE"),

    /**
     * Acknowledged a request as successful and optionally provides the requested data in the form of route parameters.
     * <p><b>Direction: </b> Client <-> Server
     * <p><b>Terminating: </b> Yes
     */
    RESPOND("RESPOND"),

    /**
     *Acknowledged a request as unsuccessful and provides a reason in the form of route parameters.
     * <p><b>Direction: </b> Client <-> Server
     * <p><b>Terminating: </b> Yes
     */
    ERROR("ERROR"),

    ;

    private final @NotNull String name;

    Method(@NotNull String name) {
        this.name = name;
    }

    public @NotNull String getName() {
        return name;
    }
}
