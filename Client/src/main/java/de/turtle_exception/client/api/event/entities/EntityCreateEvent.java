package de.turtle_exception.client.api.event.entities;

import de.turtle_exception.client.api.entities.Turtle;
import org.jetbrains.annotations.NotNull;

public interface EntityCreateEvent<T extends Turtle> {
    @NotNull T getEntity();
}
