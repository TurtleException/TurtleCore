package de.turtle_exception.core.net.route;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

public class RouteErrors {
    public static final RouteError UNKNOWN = new RouteError("Unknown error", "An unexpected internal error occurred.", null);

    public static final RouteError NOT_LOGGED_IN        = new RouteError("Not logged in", "You are not logged in.", null);
    public static final RouteError INCOMPATIBLE_VERSION = new RouteError("Incompatible Version", "Your version is not supported by the server.", null);

    public static final RouteError NOT_SUPPORTED = new RouteError("Not supported", "The requested route is not supported by this implementation", null);
    public static final RouteError BAD_REQUEST   = new RouteError("Bad request", "The requested route could be parsed but it has invalid arguments.", null);

    /* --- */

    private static final RouteError[] ERRORS;
    static {
        Field[] declaredFields = RouteError.class.getDeclaredFields();
        ArrayList<RouteError> errors = new ArrayList<>();

        for (Field field : declaredFields) {
            if (!Modifier.isStatic(field.getModifiers())) continue;
            if (field.getType() != RouteError.class)      continue;

            try {
                errors.add((RouteError) field.get(null));
            } catch (Throwable ignored) { }
        }

        ERRORS = errors.toArray(new RouteError[0]);
    }

    public static RouteError[] getErrors() {
        return ERRORS;
    }
}
