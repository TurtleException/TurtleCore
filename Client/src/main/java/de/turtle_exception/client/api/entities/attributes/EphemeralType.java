package de.turtle_exception.client.api.entities.attributes;

import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.api.event.entities.EphemeralEntityEvent;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.data.annotations.Key;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.internal.data.annotations.Types;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a Resource that is ephemeral, meaning it will not be stored in the backing database or any local cache
 * (at leas not internally). It will however, fire an {@link EphemeralEntityEvent} so that it can be processed once.
 * <p> This might be useful for resources that are used often, but don't need to be logged - especially if they would
 * amount to great sums of unnecessarily complex data.
 */
public interface EphemeralType extends Turtle {
    /**
     * Returns {@code true} if this resource is ephemeral.
     * @return {@code true} if this resource is ephemeral.
     */
    @Key(name = Keys.Attribute.EphemeralType.EPHEMERAL, sqlType = Types.Attributes.EphemeralType.EPHEMERAL)
    boolean isEphemeral();

    /**
     * Creates an Action with the instruction to modify this Resource's ephemeral status and change it to the provided boolean.
     * @param ephemeral New ephemeral state.
     * @return Action that provides the modified {@link EphemeralType} or a subtype on completion.
     */
    @NotNull Action<? extends EphemeralType> modifyEphemeral(boolean ephemeral);
}
