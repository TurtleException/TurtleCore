package de.turtle_exception.core.api.entities;

import de.turtle_exception.core.api.net.Action;
import org.jetbrains.annotations.NotNull;

public interface Group extends Turtle {
    @NotNull String getName();

    @NotNull Action<Void> modifyName(@NotNull String name);
}
