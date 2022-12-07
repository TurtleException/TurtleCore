package de.turtle_exception.client.api.entities.containers;

import de.turtle_exception.client.api.entities.Turtle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/** Represents an object that can cache {@link Turtle Turtles}. */
public interface TurtleContainer<T extends Turtle> {
    /**
     * Returns an immutable List of all cached {@link Turtle} objects.
     * @return List of cached Turtles.
     */
    @NotNull List<T> getTurtles();

    /**
     * Returns a single {@link Turtle} specified by its id, or {@code null} if no such object is stored in the
     * underlying cache.
     * @param id The unique id of the Turtle.
     * @return The requested Turtle (may be {@code null}).
     * @see Turtle#getId()
     */
    @Nullable T getTurtleById(long id);

    /**
     * Returns an immutable List of all cached {@link Turtle} objects of type {@code T}.
     * @param type The requested Turtle (may be {@code null}).
     * @return List of cached Turtles of type {@code T}.
     * @param <T1> Subclass of {@code T}.
     */
    @SuppressWarnings("unchecked")
    default <T1 extends T> @NotNull List<T1> getTurtles(@NotNull Class<T1> type) {
        ArrayList<T1> list = new ArrayList<>();
        for (T turtle : this.getTurtles())
            if (type.isInstance(turtle))
                list.add((T1) turtle);
        return List.copyOf(list);
    }

    /**
     * Returns a single {@link Turtle} of type {@code T1} specified by its id, or {@code null} if no such object is
     * stored in the underlying cache, or it is of a different type.
     * @param id The unique id of the Turtle.
     * @param type The requested Turtle (mqy be {@code null}).
     * @return The requested Turtle (may be {@code null}).
     * @param <T1> Subclass of {@code T}.
     */
    default <T1 extends T> @Nullable T1 getTurtleById(long id, @NotNull Class<T1> type) {
        T turtle = this.getTurtleById(id);
        return type.isInstance(turtle) ? type.cast(turtle) : null;
    }
}
