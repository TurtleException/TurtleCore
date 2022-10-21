package de.turtle_exception.server.data.resource;

import org.jetbrains.annotations.NotNull;

public record Field(
        @NotNull String key,
        @NotNull Class<?> type,
        boolean nullable,
        boolean primary
) { }
