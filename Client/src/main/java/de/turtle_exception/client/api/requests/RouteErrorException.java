package de.turtle_exception.client.api.requests;

import de.turtle_exception.client.internal.net.route.RouteError;
import org.jetbrains.annotations.NotNull;

public class RouteErrorException extends Exception {
    private final RouteError error;

    public RouteErrorException(@NotNull RouteError error) {
        this.error = error;
    }

    public @NotNull RouteError getRouteError() {
        return error;
    }
}
