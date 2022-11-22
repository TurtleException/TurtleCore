package de.turtle_exception.client.internal.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.internal.util.Checks;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class JsonChecks {
    private JsonChecks() { }

    public static void validateGroup(@NotNull JsonObject json) throws IllegalJsonException {
        try {
            long   id   = json.get(Keys.Turtle.ID).getAsLong();
            String name = json.get(Keys.Group.NAME).getAsString();

            Checks.nonNull(name, Keys.Group.NAME);

            validateLongArray(json.getAsJsonArray(Keys.Group.MEMBERS));
        } catch (Exception e) {
            if (e instanceof IllegalJsonException)
                throw e;
            throw new IllegalJsonException(e);
        }
    }

    public static void validateUser(@NotNull JsonObject json) throws IllegalJsonException {
        try {
            long   id   = json.get(Keys.Turtle.ID).getAsLong();
            String name = json.get(Keys.User.NAME).getAsString();

            Checks.nonNull(name, Keys.User.NAME);

            validateLongArray(json.getAsJsonArray(Keys.User.DISCORD));
            validateUUIDArray(json.getAsJsonArray(Keys.User.MINECRAFT));
        } catch (Exception e) {
            if (e instanceof IllegalJsonException)
                throw e;
            throw new IllegalJsonException(e);
        }
    }

    public static void validateTicket(@NotNull JsonObject json) throws IllegalJsonException {
        try {
            long   id             = json.get(Keys.Turtle.ID).getAsLong();
            byte   state          = json.get(Keys.Ticket.STATE).getAsByte();
            // nullable
            String title          = json.get(Keys.Ticket.TITLE).getAsString();
            String category       = json.get(Keys.Ticket.CATEGORY).getAsString();
            long   discordChannel = json.get(Keys.Ticket.DISCORD_CHANNEL).getAsLong();

            Checks.nonNull(category, Keys.Ticket.CATEGORY);

            validateStringArray(json.getAsJsonArray(Keys.Ticket.TAGS));
            validateLongArray(json.getAsJsonArray(Keys.Ticket.USERS));
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
