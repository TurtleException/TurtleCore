package de.turtle_exception.client.internal.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

public class JsonUtil {
    private JsonUtil() { }

    public static @NotNull JsonObject buildError(@NotNull String message) {
        JsonObject json = new JsonObject();
        json.addProperty("message", message);
        return json;
    }

    public static @NotNull JsonObject buildError(@NotNull Throwable t, @NotNull String message) {
        JsonObject json = buildError(message);
        json.add("exception", buildException(t));
        return json;
    }

    public static @NotNull JsonObject buildException(@NotNull Throwable t) {
        JsonArray stacktrace = new JsonArray();
        for (StackTraceElement stackTraceElement : t.getStackTrace())
            stacktrace.add(stackTraceElement.toString());

        JsonObject ex = new JsonObject();
        ex.addProperty("type", t.getClass().getName());
        ex.addProperty("message", t.getMessage());
        ex.add("stacktrace", stacktrace);

        return ex;
    }
}
