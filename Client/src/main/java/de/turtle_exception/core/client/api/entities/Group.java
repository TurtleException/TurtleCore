package de.turtle_exception.core.client.api.entities;

import de.turtle_exception.core.client.api.requests.Action;
import org.jetbrains.annotations.NotNull;

public interface Group extends Turtle {
    @NotNull String getName();

    @NotNull Action<Void> modifyName(@NotNull String name);
}
