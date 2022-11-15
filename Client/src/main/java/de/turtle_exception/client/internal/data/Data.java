package de.turtle_exception.client.internal.data;

import com.google.gson.JsonObject;
import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.internal.net.DataMethod;
import de.turtle_exception.client.internal.util.Checks;
import org.jetbrains.annotations.NotNull;

public record Data (
        @NotNull DataMethod method,
        @NotNull Class<? extends Turtle> type,
        @NotNull JsonObject content
) {
    public @NotNull JsonObject asJson() {
        JsonObject json = new JsonObject();

        json.addProperty("method", method.name());
        json.addProperty("content-type", type.getName());
        json.add("content", content.deepCopy());

        return json;
    }

    public long id() {
        return DataUtil.getTurtleId(content);
    }

    /* - - - */

    public static @NotNull Data of(@NotNull JsonObject data) throws IllegalArgumentException {
        try {
            DataMethod method  = DataMethod.of(data.get("method").getAsString());
            Class<?>   type    = Class.forName(data.get("content-type").getAsString());
            JsonObject content = ((JsonObject) data.get("content").deepCopy());

            Checks.nonNull(method , "JSON field 'method'");
            Checks.nonNull(content, "JSON field 'content'");

            return new Data(method, type.asSubclass(Turtle.class), content);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot build Data from provided JSON", e);
        }
    }

    public static @NotNull Data buildDelete(@NotNull Class<? extends Turtle> type, long id) {
        return ofIdentifierMethod(DataMethod.DELETE, type, id);
    }

    public static @NotNull Data buildGet(@NotNull Class<? extends Turtle> type, long id) {
        return ofIdentifierMethod(DataMethod.GET, type, id);
    }

    public static @NotNull Data buildPut(@NotNull Class<? extends Turtle> type, @NotNull JsonObject content) {
        return new Data(DataMethod.PUT, type, content);
    }

    public static @NotNull Data buildPatch(@NotNull Class<? extends Turtle> type, @NotNull JsonObject content) {
        return new Data(DataMethod.PATCH, type, content);
    }

    public static @NotNull Data buildUpdate(@NotNull Class<? extends Turtle> type, @NotNull JsonObject content) {
        return new Data(DataMethod.UPDATE, type, content);
    }

    public static @NotNull Data buildRemove(@NotNull Class<? extends Turtle> type, long id) {
        return ofIdentifierMethod(DataMethod.REMOVE, type, id);
    }

    private static @NotNull Data ofIdentifierMethod(@NotNull DataMethod method, @NotNull Class<? extends Turtle> type, long id) {
        JsonObject content = new JsonObject();
        content.addProperty("id", id);

        return new Data(method, type, content);
    }
}
