package de.turtle_exception.core.client.internal.net;

import de.turtle_exception.core.client.internal.TurtleClientImpl;
import de.turtle_exception.core.netcore.net.Route;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ResponseHandler {
    void handle(@NotNull Route route, @NotNull TurtleClientImpl client, @Nullable String content);
}
