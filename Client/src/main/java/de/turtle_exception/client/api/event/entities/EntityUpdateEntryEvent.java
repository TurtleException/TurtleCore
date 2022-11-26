package de.turtle_exception.client.api.event.entities;

import de.turtle_exception.client.api.entities.Turtle;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Function;

public interface EntityUpdateEntryEvent<T extends Turtle, U> {
    @NotNull T getEntity();

    @NotNull String getKey();

    @NotNull Collection<U> getCollection();

    @NotNull Function<U, Object> getMutator();
}
