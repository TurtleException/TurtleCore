package de.turtle_exception.core.client.internal.net.response;

import de.turtle_exception.core.client.internal.TurtleClientImpl;
import de.turtle_exception.core.client.internal.net.ResponseHandler;
import de.turtle_exception.core.netcore.net.Route;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ClientContentHandler implements ResponseHandler<TurtleClientImpl> {
    @Override
    public void handle(@NotNull Route route, @NotNull TurtleClientImpl core, @NotNull String connectionIdentifier, @Nullable String content) {
        // TODO
    }
}
