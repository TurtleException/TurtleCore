package de.turtle_exception.client.api.event;

import de.turtle_exception.client.api.TurtleClient;
import org.jetbrains.annotations.NotNull;

public abstract class Event {
    protected final TurtleClient client;

    public Event(@NotNull TurtleClient client) {
        this.client = client;
    }

    public @NotNull TurtleClient getClient() {
        return client;
    }
}
