package de.turtle_exception.client.internal.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.internal.net.DataMethod;
import de.turtle_exception.client.internal.util.Checks;
import org.jetbrains.annotations.NotNull;

public record Data (
        @NotNull DataMethod method,
        @NotNull Class<? extends Turtle> type,
        @NotNull JsonElement content
) {
    public @NotNull JsonObject asJson() {
        JsonObject json = new JsonObject();

        json.addProperty("method", method.name());
        json.addProperty("content-type", type.getName());
        json.add("content", content.deepCopy());

        return json;
    }

    public @NotNull JsonObject contentObject() throws ClassCastException {
        return (JsonObject) content;
    }

    public @NotNull JsonArray contentArray() throws ClassCastException {
        return (JsonArray) content;
    }

    public long id() {
        return DataUtil.getTurtleId(contentObject());
    }

    /* - - - */

    public static @NotNull Data of(@NotNull JsonObject data) throws IllegalArgumentException {
        try {
            JsonElement c = data.get("content");

            DataMethod  method  = DataMethod.of(data.get("method").getAsString());
            Class<?>    type    = Class.forName(data.get("content-type").getAsString());
            JsonElement content = c != null ? c.deepCopy() : JsonNull.INSTANCE;

            Checks.nonNull(method , "JSON field 'method'");
            Checks.nonNull(content, "JSON field 'content'");

            return new Data(method, type.asSubclass(Turtle.class), content);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot build Data from provided JSON", e);
        }
    }

    public static @NotNull Data buildDelete(@NotNull Class<? extends Turtle> type, long id) {
        return new Data(DataMethod.DELETE, type, buildJson("id", id));
    }

    public static @NotNull Data buildGet(@NotNull Class<? extends Turtle> type, long id) {
        return new Data(DataMethod.GET, type, buildJson("id", id));
    }

    public static @NotNull Data buildGet(@NotNull Class<? extends Turtle> type) {
        return new Data(DataMethod.GET, type, JsonNull.INSTANCE);
    }

    public static @NotNull Data buildPut(@NotNull Class<? extends Turtle> type, @NotNull JsonObject content) {
        return new Data(DataMethod.PUT, type, content);
    }

    public static @NotNull Data buildPatch(@NotNull Class<? extends Turtle> type, @NotNull JsonObject content) {
        return new Data(DataMethod.PATCH, type, content);
    }

    public static @NotNull Data buildPatchEntryAdd(@NotNull Class<? extends Turtle> type, long id, @NotNull String key, @NotNull Object obj) {
        return new Data(DataMethod.PATCH_ENTRY_ADD, type, buildJson("id", id, "key", key, "val", obj));
    }

    public static @NotNull Data buildPatchEntryDel(@NotNull Class<? extends Turtle> type, long id, @NotNull String key, @NotNull Object obj) {
        return new Data(DataMethod.PATCH_ENTRY_DEL, type, buildJson("id", id, "key", key, "val", obj));
    }

    public static @NotNull Data buildUpdate(@NotNull Class<? extends Turtle> type, @NotNull JsonElement content) {
        return new Data(DataMethod.UPDATE, type, content);
    }

    public static @NotNull Data buildRemove(@NotNull Class<? extends Turtle> type, long id) {
        return new Data(DataMethod.REMOVE, type, buildJson("id", id));
    }

    private static @NotNull JsonObject buildJson(@NotNull Object... args) throws IllegalArgumentException {
        if (args.length % 2 == 1)
            throw new IllegalArgumentException("Must provide an even amount of arguments");

        JsonObject json = new JsonObject();
        for (int i = 0; i < args.length; i = i + 2)
            DataUtil.addValue(json, String.valueOf(args[i]), args[i + 1]);
        return json;
    }
}
