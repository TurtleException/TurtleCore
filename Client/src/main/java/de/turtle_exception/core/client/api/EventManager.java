package de.turtle_exception.core.client.api;

import de.turtle_exception.core.client.api.events.Event;
import org.jetbrains.annotations.NotNull;

public interface EventManager {
    void handleEvent(@NotNull Event event);

    void register(@NotNull Listener listener);

    void unregister(@NotNull Listener listener);
}
