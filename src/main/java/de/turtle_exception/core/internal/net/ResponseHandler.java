package de.turtle_exception.core.internal.net;

import de.turtle_exception.core.internal.TurtleCore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ResponseHandler<T extends TurtleCore> {
    void handle(@NotNull Route route, @NotNull T core, @NotNull String connectionIdentifier, @Nullable String content);
}
