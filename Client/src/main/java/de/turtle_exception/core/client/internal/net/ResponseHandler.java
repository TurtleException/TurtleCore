package de.turtle_exception.core.client.internal.net;

import de.turtle_exception.core.client.internal.TurtleCore;
import de.turtle_exception.core.netcore.net.Route;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ResponseHandler<T extends TurtleCore> {
    void handle(@NotNull Route route, @NotNull T core, @NotNull String connectionIdentifier, @Nullable String content);
}
