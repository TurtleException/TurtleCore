package de.turtle_exception.client.internal.entities;

import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.Turtle;
import org.jetbrains.annotations.NotNull;

abstract class TurtleImpl implements Turtle {
    protected final @NotNull TurtleClient client;
    protected final long id;

    protected TurtleImpl(@NotNull TurtleClient client, long id) {
        this.client = client;
        this.id = id;
    }

    @Override
    public long getId() {
        return id;
    }

    @NotNull
    @Override
    public TurtleClient getClient() {
        return client;
    }
}
