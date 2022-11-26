package de.turtle_exception.client.api.event.entities;

import de.turtle_exception.client.api.entities.Turtle;
import org.jetbrains.annotations.NotNull;

public interface EntityDeleteEvent<T extends Turtle> {
    @NotNull T getEntity();
}
