package de.turtle_exception.client.internal.net;

import com.google.gson.JsonObject;
import de.turtle_exception.client.internal.data.Provider;
import org.jetbrains.annotations.NotNull;

public class NetworkProvider implements Provider {
    @Override
    public boolean delete(@NotNull Class<?> type, @NotNull Object... primary) {

    }

    @Override
    public @NotNull JsonObject get(@NotNull Class<?> type, @NotNull Object... primary) {

    }

    @Override
    public @NotNull JsonObject put(@NotNull Class<?> type, @NotNull JsonObject data) {

    }

    @Override
    public @NotNull JsonObject patch(@NotNull Class<?> type, @NotNull JsonObject data, @NotNull Object... primary) {

    }
}
