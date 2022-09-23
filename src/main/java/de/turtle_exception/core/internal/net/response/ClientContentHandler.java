package de.turtle_exception.core.internal.net.response;

import de.turtle_exception.core.internal.TurtleClientImpl;
import de.turtle_exception.core.internal.net.ResponseHandler;
import de.turtle_exception.core.internal.net.Route;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ClientContentHandler implements ResponseHandler<TurtleClientImpl> {
    @Override
    public void handle(@NotNull Route route, @NotNull TurtleClientImpl core, @NotNull String connectionIdentifier, @Nullable String content) {
        // TODO
    }
}
