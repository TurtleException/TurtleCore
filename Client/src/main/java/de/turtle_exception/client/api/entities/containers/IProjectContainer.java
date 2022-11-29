package de.turtle_exception.client.api.entities.containers;

import de.turtle_exception.client.api.entities.Project;
import de.turtle_exception.client.api.entities.Turtle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/** Represents an object that can cache {@link Project Projects}. */
public interface IProjectContainer extends ITurtleContainer {
    /**
     * Returns an immutable List of all cached {@link Project} objects.
     * @return List of cached Projects.
     */
    @NotNull List<Project> getProjects();

    /**
     * Returns a single {@link Project} specified by its id, or {@code null} if no such object is stored in the
     * underlying cache.
     * @param id The unique id of the Project.
     * @return The requested Project (may be {@code null}).
     * @see Project#getId()
     */
    @Nullable Project getProjectById(long id);

    @Override
    default @NotNull List<Turtle> getTurtles() {
        return List.copyOf(new ArrayList<>(getProjects()));
    }

    @Override
    default @Nullable Turtle getTurtleById(long id) {
        return getProjectById(id);
    }
}
