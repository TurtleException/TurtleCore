package de.turtle_exception.core.core.net.route;

public class Errors {
    // LOGIN & VERSION
    public static final String NOT_LOGGED_IN = "NOT LOGGED IN";
    public static final String INCOMPATIBLE_VERSION = "INCOMPATIBLE VERSION";

    /** The requested route is not supported in this implementation. */
    public static final String NOT_SUPPORTED = "NOT SUPPORTED";
    /** The requested route could be handled, but it has invalid arguments */
    public static final String BAD_REQUEST   = "BAS REQUEST";

    // should be used to append serialized exception
    public static final String EXCEPTION = "EXCEPTION";

    // DATA
    public static final String DATA_SERVICE_INTERNAL = "DATA SERVICE INTERNAL";
    public static final String ILLEGAL_ID = "ILLEGAL ID";
}
