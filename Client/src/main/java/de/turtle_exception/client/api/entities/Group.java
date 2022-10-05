package de.turtle_exception.client.api.entities;

import de.turtle_exception.client.api.entities.attribute.IUserContainer;
import de.turtle_exception.client.api.requests.Action;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public interface Group extends Turtle, IUserContainer {
    @NotNull String getName();

    @NotNull Action<Void> modifyName(@NotNull String name);
}
