package de.turtle_exception.client.api.event.entities.json_resource;

import de.turtle_exception.client.api.entities.JsonResource;
import de.turtle_exception.client.api.event.entities.EntityDeleteEvent;
import org.jetbrains.annotations.NotNull;

public class JsonResourceDeleteEvent extends JsonResourceEvent implements EntityDeleteEvent<JsonResource> {
    public JsonResourceDeleteEvent(@NotNull JsonResource jsonResource) {
        super(jsonResource);
    }
}
