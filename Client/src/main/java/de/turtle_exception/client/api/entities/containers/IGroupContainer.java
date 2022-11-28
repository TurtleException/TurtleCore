package de.turtle_exception.client.api.entities.containers;

import de.turtle_exception.client.api.entities.Group;
import de.turtle_exception.client.api.entities.Turtle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/** Represents an object that can cache {@link Group Groups}. */
public interface IGroupContainer extends ITurtleContainer {
    /**
     * Returns an immutable List of all cached {@link Group} objects.
     * @return List of cached Groups.
     */
    @NotNull List<Group> getGroups();

    /**
     * Returns a single {@link Group} specified by its id, or {@code null} if no such object is stored in the
     * underlying cache.
     * @param id The unique id of the Group.
     * @return The requested Group (may be {@code null}).
     * @see Group#getId()
     */
    @Nullable Group getGroupById(long id);

    @Override
    default @NotNull List<Turtle> getTurtles() {
        return List.copyOf(new ArrayList<>(getGroups()));
    }

    @Override
    default @Nullable Turtle getTurtleById(long id) {
        return getGroupById(id);
    }
}
