package de.turtle_exception.core.client.internal.net.response;

import de.turtle_exception.core.client.internal.TurtleCore;
import de.turtle_exception.core.client.internal.net.Route;
import de.turtle_exception.core.client.internal.net.ResponseHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class QuitHandler implements ResponseHandler<TurtleCore> {
    @Override
    public void handle(@NotNull Route route, @NotNull TurtleCore core, @NotNull String connectionIdentifier, @Nullable String content) {
        // TODO
    }
}
