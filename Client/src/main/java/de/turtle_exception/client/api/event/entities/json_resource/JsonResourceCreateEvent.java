package de.turtle_exception.client.api.event.entities.json_resource;

import de.turtle_exception.client.api.entities.JsonResource;
import de.turtle_exception.client.api.event.entities.EntityCreateEvent;
import org.jetbrains.annotations.NotNull;

public class JsonResourceCreateEvent extends JsonResourceEvent implements EntityCreateEvent<JsonResource> {
    public JsonResourceCreateEvent(@NotNull JsonResource jsonResource) {
        super(jsonResource);
    }
}
