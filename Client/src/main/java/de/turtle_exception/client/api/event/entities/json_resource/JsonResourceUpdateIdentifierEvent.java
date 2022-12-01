package de.turtle_exception.client.api.event.entities.json_resource;

import de.turtle_exception.client.api.entities.JsonResource;
import de.turtle_exception.client.internal.data.annotations.Keys;
import org.jetbrains.annotations.NotNull;

public class JsonResourceUpdateIdentifierEvent extends JsonResourceUpdateEvent<String> {
    public JsonResourceUpdateIdentifierEvent(@NotNull JsonResource jsonResource, String oldValue, String newValue) {
        super(jsonResource, Keys.JsonResource.IDENTIFIER, oldValue, newValue);
    }
}
