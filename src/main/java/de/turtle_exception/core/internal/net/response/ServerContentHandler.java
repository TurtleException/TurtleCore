package de.turtle_exception.core.internal.net.response;

import de.turtle_exception.core.internal.TurtleServerImpl;
import de.turtle_exception.core.internal.net.ResponseHandler;
import de.turtle_exception.core.internal.net.Route;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ServerContentHandler implements ResponseHandler<TurtleServerImpl> {
    @Override
    public void handle(@NotNull Route route, @NotNull TurtleServerImpl core, @NotNull String connectionIdentifier, @Nullable String content) {
        // TODO
    }
}
