package de.turtle_exception.client.internal.data;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

/** Source of data */
public interface Provider {
    boolean delete(@NotNull Class<?> type, @NotNull Object... primary);

    @NotNull JsonObject get(@NotNull Class<?> type, @NotNull Object... primary);

    @NotNull JsonObject put(@NotNull Class<?> type, @NotNull JsonObject data);

    @NotNull JsonObject patch(@NotNull Class<?> type, @NotNull JsonObject data, @NotNull Object... primary);
}
