package de.turtle_exception.core.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.turtle_exception.core.util.Checks;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class JsonChecks {
    private JsonChecks() { }

    public static void validateGroup(@NotNull JsonObject json) throws IllegalJsonException {
        try {
            long id     = json.get("id").getAsLong();
            String name = json.get("name").getAsString();

            Checks.nonNull(name, "Name");

            validateLongArray(json.getAsJsonArray("members"));
        } catch (Exception e) {
            if (e instanceof IllegalJsonException)
                throw e;
            throw new IllegalJsonException(e);
        }
    }

    public static void validateUser(@NotNull JsonObject json) throws IllegalJsonException {
        try {
            long id     = json.get("id").getAsLong();
            String name = json.get("name").getAsString();

            Checks.nonNull(name, "Name");

            validateLongArray(json.getAsJsonArray("discord"));
            validateUUIDArray(json.getAsJsonArray("minecraft"));
        } catch (Exception e) {
            if (e instanceof IllegalJsonException)
                throw e;
            throw new IllegalJsonException(e);
        }
    }

    public static void validateLongArray(@NotNull JsonArray json) throws IllegalJsonException {
        for (JsonElement element : json) {
            try {
                element.getAsLong();
            } catch (Exception e) {
                throw new IllegalJsonException("Illegal entry: " + element);
            }
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void validateUUIDArray(@NotNull JsonArray json) throws IllegalJsonException {
        for (JsonElement element : json) {
            try {
                UUID.fromString(element.getAsString());
            } catch (Exception e) {
                throw new IllegalJsonException("Illegal entry: " + element);
            }
        }
    }
}
