package de.turtle_exception.client.api.entities;

import de.turtle_exception.client.api.requests.Action;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public interface Group extends Turtle {
    @NotNull String getName();

    @NotNull Action<Void> modifyName(@NotNull String name);
}
