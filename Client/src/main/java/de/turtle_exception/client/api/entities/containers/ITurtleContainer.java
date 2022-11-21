package de.turtle_exception.client.api.entities.containers;

import de.turtle_exception.client.api.entities.Turtle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ITurtleContainer {
    @NotNull List<Turtle> getTurtles();

    @Nullable Turtle getTurtleById(long id);

    default <T extends Turtle> @Nullable T getTurtleById(long id, @NotNull Class<T> type) {
        Turtle turtle = this.getTurtleById(id);
        return type.isInstance(turtle) ? type.cast(turtle) : null;
    }
}
