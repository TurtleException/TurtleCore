package de.turtle_exception.client.internal.util;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class JsonUtil {
    private JsonUtil() { }

    /**
     * Converts a String representation of a JSON object into a {@link Map} with Strings as keys and values. Values may
     * be other (JSON) objects themselves. The String representation of values has been chosen because it is the only
     * type where its safe to assume that no conversion errors may occur.
     */
    public static @NotNull Map<String, String> jsonToMap(@NotNull String json) {
        return new Gson().fromJson(json, new TypeToken<Map<String, String>>(){}.getType());
    }

    /**
     * Converts a String representation of a JSON array into a {@link List}. The String representation of elements has
     * been chosen because it is the only type where its safe to assume that no conversion errors may occur.
     */
    public static @NotNull List<String> jsonToList(@NotNull String json) {
        return new Gson().fromJson(json, new TypeToken<List<String>>(){}.getType());
    }
}
