package de.turtle_exception.client.api.event.entities.json_resource;

import com.google.gson.JsonElement;
import de.turtle_exception.client.api.entities.JsonResource;
import de.turtle_exception.client.internal.data.annotations.Keys;
import org.jetbrains.annotations.NotNull;

public class JsonResourceUpdateContentEvent extends JsonResourceUpdateEvent<JsonElement> {
    public JsonResourceUpdateContentEvent(@NotNull JsonResource jsonResource, JsonElement oldValue, JsonElement newValue) {
        super(jsonResource, Keys.JsonResource.CONTENT, oldValue, newValue);
    }
}
