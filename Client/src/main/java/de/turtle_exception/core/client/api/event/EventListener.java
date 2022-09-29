package de.turtle_exception.core.client.api.event;

import org.jetbrains.annotations.NotNull;

public abstract class EventListener {
    public void onEvent(@NotNull Event event) { }
}
