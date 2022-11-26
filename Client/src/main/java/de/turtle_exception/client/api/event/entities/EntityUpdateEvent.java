package de.turtle_exception.client.api.event.entities;

import de.turtle_exception.client.api.entities.Turtle;
import org.jetbrains.annotations.NotNull;

public interface EntityUpdateEvent<T extends Turtle, V> {
    @NotNull T getEntity();

    @NotNull String getKey();

    V getOldValue();

    V getNewValue();
}
