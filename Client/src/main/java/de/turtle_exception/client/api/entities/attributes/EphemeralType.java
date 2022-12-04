package de.turtle_exception.client.api.entities.attributes;

import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.data.annotations.Key;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.internal.data.annotations.Types;
import org.jetbrains.annotations.NotNull;

public interface EphemeralType {
    // TODO: update this when the event-thing is implemented
    /**
     * Returns true if this resource is ephemeral.
     * <p> When a resource is ephemeral it will not be cached by clients (at least not internally) or the server. It
     * will however, fire an event so that it can be processed once.
     * <p> This might be useful for resources that are used often, but don't need to be logged - especially if they
     * would amount to great sums of unnecessarily complex data.
     * @return {@code true} if this resource is ephemeral.
     */
    @Key(name = Keys.Attribute.EphemeralType.EPHEMERAL, sqlType = Types.Attributes.EphemeralType.EPHEMERAL)
    boolean isEphemeral();

    @NotNull Action<? extends EphemeralType> modifyEphemeral(boolean ephemeral);
}
