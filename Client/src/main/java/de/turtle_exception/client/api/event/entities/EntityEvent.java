package de.turtle_exception.client.api.event.entities;

import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.api.event.Event;
import org.jetbrains.annotations.NotNull;

public abstract class EntityEvent<T extends Turtle> extends Event {
    protected final @NotNull T entity;

    public EntityEvent(@NotNull T entity) {
        super(entity.getClient());
        this.entity = entity;
    }

    public @NotNull T getEntity() {
        return entity;
    }
}
