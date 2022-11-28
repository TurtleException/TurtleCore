package de.turtle_exception.client.api.entities.containers;

import de.turtle_exception.client.api.entities.Turtle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/** Represents an object that can cache {@link Turtle Turtles}. */
public interface ITurtleContainer {
    /**
     * Returns an immutable List of all cached {@link Turtle} objects.
     * @return List of cached Turtles.
     */
    @NotNull List<Turtle> getTurtles();

    /**
     * Returns a single {@link Turtle} specified by its id, or {@code null} if no such object is stored in the
     * underlying cache.
     * @param id The unique id of the Turtle.
     * @return The requested Turtle (may be {@code null}).
     * @see Turtle#getId()
     */
    @Nullable Turtle getTurtleById(long id);

    /**
     * Returns a single {@link Turtle} of type {@code T} specified by its id, or {@code null} if no such object is
     * stored in the underlying cache, or it is of a different type.
     * @param id The unique id of the Turtle.
     * @param type THe requested Turtle (mqy be {@code null}).
     * @return The requested Turtle (may be {@code null}).
     * @param <T> Subclass of {@link Turtle}
     */
    default <T extends Turtle> @Nullable T getTurtleById(long id, @NotNull Class<T> type) {
        Turtle turtle = this.getTurtleById(id);
        return type.isInstance(turtle) ? type.cast(turtle) : null;
    }
}
