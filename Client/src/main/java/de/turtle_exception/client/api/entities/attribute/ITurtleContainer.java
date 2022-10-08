package de.turtle_exception.client.api.entities.attribute;

import de.turtle_exception.client.api.entities.Turtle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ITurtleContainer {
    @NotNull List<Turtle> getTurtles();

    @Nullable Turtle getTurtleById(long id);
}
