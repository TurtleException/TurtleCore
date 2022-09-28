package de.turtle_exception.core.netcore.net.route;

import org.jetbrains.annotations.NotNull;

// TODO: docs
public record CompiledRoute(
        int callbackCode,
        @NotNull String command,
        @NotNull ContentType contentType,
        @NotNull String content,
        boolean terminating
) { }
