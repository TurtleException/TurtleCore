package de.turtle_exception.core.client.api.event;

import de.turtle_exception.core.client.api.TurtleClient;
import org.jetbrains.annotations.NotNull;

public class Event {
    protected final TurtleClient client;

    public Event(@NotNull TurtleClient client) {
        this.client = client;
    }

    public @NotNull TurtleClient getClient() {
        return client;
    }
}
