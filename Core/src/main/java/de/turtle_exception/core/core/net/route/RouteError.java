package de.turtle_exception.core.core.net.route;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RouteError {
    private final String     message;
    private final String     description;
    private final Throwable  throwable;
    private final JsonObject jsonThrowable;

    RouteError(@NotNull String message, @Nullable String description, @Nullable Throwable throwable) {
        this(message, description, throwable, null);
    }

    private RouteError(@NotNull String message, @Nullable String description, @Nullable Throwable throwable, @Nullable JsonObject throwableJson) {
        this.message       = message;
        this.description   = description;
        this.throwable     = throwable;
        this.jsonThrowable = throwableJson;
    }

    /* - - - */

    // helper for prebuilt errors
    public RouteError with(@NotNull Throwable throwable) {
        return new RouteError(message, description, throwable);
    }

    private RouteError with(@NotNull JsonObject throwableJson) {
        return new RouteError(message, description, throwable, throwableJson);
    }

    /* - - - */

    public static RouteError of(JsonElement json) throws IllegalArgumentException {
        if (json == null)
            throw new IllegalArgumentException("Empty JSON");
        if (!json.isJsonObject())
            throw new IllegalArgumentException("Illegal type of JsonElement: " + json.getClass().getSimpleName());

        JsonObject content   = json.getAsJsonObject();
        String message       = content.get("message").getAsString();
        JsonObject throwable = content.getAsJsonObject("throwable");

        if (message == null)
            throw new IllegalArgumentException("Illegal message: null");

        RouteError error = null;
        for (RouteError checkError : RouteErrors.getErrors()) {
            if (!checkError.getMessage().equals(message)) continue;

            error = checkError;
            break;
        }

        if (error == null)
            throw new IllegalArgumentException("Unknown error: " + message);

        return error.with(throwable);
    }

    /* - - - */

    public CompiledRoute compile() {
        JsonObject json = new JsonObject();

        json.addProperty("message", message);
        if (description != null)
            json.addProperty("description", description);
        if (throwable != null)
            json.add("throwable", parseThrowable(throwable));

        return Routes.ERROR.compile(json);
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

    /* - - - */

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public JsonObject getJsonThrowable() {
        return jsonThrowable;
    }
}
