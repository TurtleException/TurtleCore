package de.turtle_exception.core.netcore.net.route;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// TODO: docs
public record CompiledRoute(
        int callbackCode,
        @Nullable String command,
        @NotNull ContentType contentType,
        @NotNull String content,
        boolean terminating
) { }
