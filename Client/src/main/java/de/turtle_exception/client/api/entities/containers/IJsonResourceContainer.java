package de.turtle_exception.client.api.entities.containers;

import de.turtle_exception.client.api.entities.JsonResource;
import de.turtle_exception.client.api.entities.Turtle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/** Represents an object that can cache {@link JsonResource JsonResources}. */
public interface IJsonResourceContainer extends ITurtleContainer {
    /**
     * Returns an immutable List of all cached {@link JsonResource} objects.
     * @return List of cached JsonResources.
     */
    @NotNull List<JsonResource> getJsonResources();

    /**
     * Returns a single {@link JsonResource} specified by its id, or {@code null} if no such object is stored in the
     * underlying cache.
     * @param id The unique id of the JsonResource.
     * @return The requested JsonResource (may be {@code null}).
     * @see JsonResource#getId()
     */
    @Nullable JsonResource getJsonResourceById(long id);

    @Override
    default @NotNull List<Turtle> getTurtles() {
        return List.copyOf(new ArrayList<>(getJsonResources()));
    }

    @Override
    default @Nullable Turtle getTurtleById(long id) {
        return getJsonResourceById(id);
    }
}
