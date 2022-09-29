package de.turtle_exception.core.client.api;

import de.turtle_exception.core.client.api.event.events.Event;
import org.jetbrains.annotations.NotNull;

public abstract class Listener {
    public void onEvent(@NotNull Event event) { }
}
