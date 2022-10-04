package de.turtle_exception.core.core.net.route;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// TODO: docs
public record CompiledRoute(
        @NotNull Method method,
        @NotNull String route,
        @Nullable String content
) { }
