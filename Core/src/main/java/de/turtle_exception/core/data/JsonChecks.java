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
            long   id          = json.get("id").getAsLong();
            String name        = json.get("name").getAsString();
            long   permissions = json.get("permissions").getAsLong();

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
            long   id          = json.get("id").getAsLong();
            String name        = json.get("name").getAsString();
            long   permissions = json.get("permissions").getAsLong();

            Checks.nonNull(name, "Name");

            validateLongArray(json.getAsJsonArray("discord"));
            validateUUIDArray(json.getAsJsonArray("minecraft"));
        } catch (Exception e) {
            if (e instanceof IllegalJsonException)
                throw e;
            throw new IllegalJsonException(e);
        }
    }

    public static void validateTicket(@NotNull JsonObject json) throws IllegalJsonException {
        try {
            long   id             = json.get("id").getAsLong();
            byte   state          = json.get("state").getAsByte();
            // nullable
            String title          = json.get("title").getAsString();
            String category       = json.get("category").getAsString();
            long   discordChannel = json.get("discord_channel").getAsLong();

            Checks.nonNull(category, "Category");

            validateStringArray(json.getAsJsonArray("tags"));
            validateLongArray(json.getAsJsonArray("users"));
        } catch (Exception e) {
            if (e instanceof IllegalJsonException)
                throw e;
            throw new IllegalJsonException(e);
        }
    }

    public static void validateStringArray(@NotNull JsonArray json) throws IllegalJsonException {
        for (JsonElement element : json) {
            try {
                element.getAsString();
            } catch (Exception e) {
                throw new IllegalJsonException("Illegal entry: " + element);
            }
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
