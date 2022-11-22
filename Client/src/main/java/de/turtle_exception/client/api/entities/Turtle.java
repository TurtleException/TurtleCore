package de.turtle_exception.client.api.entities;

import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.TurtleClientImpl;
import de.turtle_exception.client.internal.data.annotations.Key;
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
    @Key(name = Keys.Turtle.ID)
    long getId();

    // TODO: docs
    @NotNull TurtleClient getClient();

    @NotNull
    default Action<Boolean> delete() {
        return getClient().getProvider().delete(this).onSuccess(b -> {
            ((TurtleClientImpl) getClient()).removeTurtle(this);
        });
    }

    @NotNull
    Action<? extends Turtle> update();
}
