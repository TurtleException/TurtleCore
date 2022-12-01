package de.turtle_exception.client.api.event.entities.json_resource;

import de.turtle_exception.client.api.entities.JsonResource;
import de.turtle_exception.client.api.event.entities.EntityEvent;
import org.jetbrains.annotations.NotNull;

public abstract class JsonResourceEvent extends EntityEvent<JsonResource> {
    public JsonResourceEvent(@NotNull JsonResource jsonResource) {
        super(jsonResource);
    }

    public @NotNull JsonResource getJsonResource() {
        return this.getEntity();
    }
}
