package de.turtle_exception.client.api.entities;

import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.TurtleClientImpl;
import de.turtle_exception.client.internal.data.annotations.Key;
import de.turtle_exception.client.internal.data.annotations.Resource;
import org.jetbrains.annotations.NotNull;

/**
 * A turtle entity is an entity that can be uniquely identified by its turtle id.
 * @implNote All implementations of this interface must have the {@link Resource} annotation.
 * @see Turtle#getId()
 */
public interface Turtle {
    /**
     * Provides the unique turtle id of this entity. This id should never change and always only reference this entity.
     * @return Long representation of the id.
     */
    @Key(name = Keys.Turtle.ID)
    long getId();

    /**
     * Provides the {@link TurtleClient} instance responsible for this Turtle object. The TurtleClient handles caching
     * of resources and provides API methods to use, create or retrieve them.
     * @return Responsible TurtleClient
     */
    @NotNull TurtleClient getClient();

    /**
     * Creates an Action with the instruction to delete this turtle resource from the server-side database.
     * <p> If the operation is successful, the {@link TurtleClient} will also remove any cache references to this object.
     * @return Deletion Action
     */
    @NotNull
    default Action<Boolean> delete() {
        return getClient().getProvider().delete(this).onSuccess(b -> {
            ((TurtleClientImpl) getClient()).removeTurtle(this);
        });
    }

    /**
     * Creates an Action with the Provider request to re-send the data of this resource.
     * <p> If the operation is successful, the {@link TurtleClient} will also update any cache references to this resource.
     * <p> This will usually return the object itself, but with modified attributes, though this behaviour is not
     * guaranteed; the value provided by the completed action should replace any old representation of this resource.
     * @return The new / updated object.
     */
    @NotNull
    Action<? extends Turtle> update();
}
