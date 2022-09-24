package de.turtle_exception.core.client.internal.net.response;

import de.turtle_exception.core.client.internal.TurtleServerImpl;
import de.turtle_exception.core.client.internal.net.ResponseHandler;
import de.turtle_exception.core.netcore.net.Route;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VersionHandler implements ResponseHandler<TurtleServerImpl> {
    @Override
    public void handle(@NotNull Route route, @NotNull TurtleServerImpl server, @NotNull String clientIdentifier, @Nullable String content) {
        // TODO
    }
}
