package de.turtle_exception.client.internal.data;

import com.google.gson.JsonObject;
import de.turtle_exception.client.internal.data.annotations.Key;
import de.turtle_exception.client.internal.net.DataMethod;
import de.turtle_exception.client.internal.util.Checks;
import org.jetbrains.annotations.NotNull;

public record Data(
        @NotNull DataMethod method,
        @NotNull Class<?> type,
        @NotNull JsonObject content
) {
    public @NotNull JsonObject asJson() {
        JsonObject json = new JsonObject();

        json.addProperty("method", method.name());
        json.addProperty("content-type", type.getName());
        json.add("content", content.deepCopy());

        return json;
    }

    public @NotNull Object primary() {
        return DataUtil.getPrimaryValue(content, type);
    }

    /* - - - */

    public static @NotNull Data of(@NotNull JsonObject data) throws IllegalArgumentException {
        try {
            DataMethod method  = DataMethod.of(data.get("method").getAsString());
            Class<?>   type    = Class.forName(data.get("content-type").getAsString());
            JsonObject content = ((JsonObject) data.get("content").deepCopy());

            Checks.nonNull(method , "JSON field 'method'");
            Checks.nonNull(content, "JSON field 'content'");

            return new Data(method, type, content);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot build Data from provided JSON", e);
        }
    }

    public static @NotNull Data buildDelete(@NotNull Class<?> type, @NotNull Object primary) {
        return ofIdentifierMethod(DataMethod.DELETE, type, primary);
    }

    public static @NotNull Data buildGet(@NotNull Class<?> type, @NotNull Object primary) {
        return ofIdentifierMethod(DataMethod.GET, type, primary);
    }

    public static @NotNull Data buildPut(@NotNull Class<?> type, @NotNull JsonObject content) {
        return new Data(DataMethod.PUT, type, content);
    }

    public static @NotNull Data buildPatch(@NotNull Class<?> type, @NotNull JsonObject content) {
        return new Data(DataMethod.PATCH, type, content);
    }

    public static @NotNull Data buildUpdate(@NotNull Class<?> type, @NotNull JsonObject content) {
        return new Data(DataMethod.UPDATE, type, content);
    }

    public static @NotNull Data buildRemove(@NotNull Class<?> type, @NotNull Object primary) {
        return ofIdentifierMethod(DataMethod.REMOVE, type, primary);
    }

    private static @NotNull Data ofIdentifierMethod(@NotNull DataMethod method, @NotNull Class<?> type, @NotNull Object primary) {
        Key key = DataUtil.getPrimary(type).getAnnotation(Key.class);

        JsonObject content = new JsonObject();
        DataUtil.addValue(content, key.name(), primary);

        return new Data(method, type, content);
    }
}
