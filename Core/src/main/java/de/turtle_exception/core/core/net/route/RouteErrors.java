package de.turtle_exception.core.core.net.route;

public class RouteErrors {
    public static final RouteError UNKNOWN = new RouteError("Unknown error", "An unexpected internal error occurred.", null);

    public static final RouteError NOT_LOGGED_IN        = new RouteError("Not logged in", "You are not logged in.", null);
    public static final RouteError INCOMPATIBLE_VERSION = new RouteError("Incompatible Version", "Your version is not supported by the server.", null);

    public static final RouteError NOT_SUPPORTED = new RouteError("Not supported", "The requested route is not supported by this implementation", null);
    public static final RouteError BAD_REQUEST   = new RouteError("Bad request", "The requested route could be parsed but it has invalid arguments.", null);
}
