package de.turtle_exception.client.api.entities;

import de.turtle_exception.client.api.TurtleClient;
import org.jetbrains.annotations.NotNull;

/**
 * A turtle entity is an entity that can be uniquely identified by its turtle id.
 * @see Turtle#getId()
 */
public interface Turtle {
    /**
     * Provides the unique turtle id of this entity. This id should never change and always only reference this entity.
     * @return Long representation of the id.
     */
    long getId();

    // TODO: docs
    @NotNull TurtleClient getClient();
}
