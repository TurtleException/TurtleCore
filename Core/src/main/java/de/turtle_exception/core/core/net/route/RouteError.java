package de.turtle_exception.core.core.net.route;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RouteError {
    private final String    message;
    private final String    description;
    private final Throwable throwable;

    RouteError(@Nullable String message, @Nullable String description, @Nullable Throwable throwable) throws IllegalArgumentException {
        this.message = message;
        this.description = description;
        this.throwable = throwable;

        if (message == null && throwable == null)
            throw new IllegalArgumentException("Either message or throwable must be non-null!");
    }

    /* - - - */

    // helper for prebuilt errors
    public RouteError with(@NotNull Throwable throwable) {
        return new RouteError(message, description, throwable);
    }

    /* - - - */

    public CompiledRoute compile() {
        JsonObject json = new JsonObject();

        if (message != null)
            json.addProperty("message", message);
        if (description != null)
            json.addProperty("description", description);
        if (throwable != null)
            json.add("throwable", parseThrowable(throwable));

        return Routes.ERROR.compile(json.toString());
    }

    private static @NotNull JsonObject parseThrowable(@NotNull Throwable throwable) {
        JsonObject json = new JsonObject();

        json.addProperty("type", throwable.getClass().getName());
        json.addProperty("message", throwable.getMessage());

        JsonArray stackTrace = new JsonArray();
        for (StackTraceElement stackTraceElement : throwable.getStackTrace())
            stackTrace.add(stackTraceElement.toString());
        json.add("stacktrace", stackTrace);

        // TODO: should this have a limit?
        Throwable cause = throwable.getCause();
        if (cause != null)
            json.add("cause", parseThrowable(cause));

        return json;
    }
}
