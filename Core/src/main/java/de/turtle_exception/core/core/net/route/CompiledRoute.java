package de.turtle_exception.core.core.net.route;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// TODO: docs
public record CompiledRoute(
        @NotNull Route route,
        @NotNull Method method,
        @NotNull String routeStr,
        @Nullable String content
) {
    public boolean isRoute(@NotNull Route route) {
        return this.route.getRoute().equals(route.getRoute());
    }
}
