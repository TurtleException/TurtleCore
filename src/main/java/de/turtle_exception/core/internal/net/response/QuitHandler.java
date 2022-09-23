package de.turtle_exception.core.internal.net.response;

import de.turtle_exception.core.internal.TurtleCore;
import de.turtle_exception.core.internal.net.ResponseHandler;
import de.turtle_exception.core.internal.net.Route;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class QuitHandler implements ResponseHandler<TurtleCore> {
    @Override
    public void handle(@NotNull Route route, @NotNull TurtleCore core, @NotNull String connectionIdentifier, @Nullable String content) {
        // TODO
    }
}
