package de.turtle_exception.client.api.event.entities.json_resource;

import de.turtle_exception.client.api.entities.JsonResource;
import de.turtle_exception.client.internal.data.annotations.Keys;
import org.jetbrains.annotations.NotNull;

public class JsonResourceUpdateEphemeralEvent extends JsonResourceUpdateEvent<Boolean> {
    public JsonResourceUpdateEphemeralEvent(@NotNull JsonResource jsonResource, Boolean oldValue, Boolean newValue) {
        super(jsonResource, Keys.Attribute.EphemeralType.EPHEMERAL, oldValue, newValue);
    }
}
