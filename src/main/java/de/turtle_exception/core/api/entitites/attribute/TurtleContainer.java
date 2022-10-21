package de.turtle_exception.core.api.entitites.attribute;

import de.turtle_exception.core.api.entitites.Turtle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface TurtleContainer {
    @NotNull List<Turtle> getTurtles();

    @Nullable Turtle getTurtleById(long id);
}
