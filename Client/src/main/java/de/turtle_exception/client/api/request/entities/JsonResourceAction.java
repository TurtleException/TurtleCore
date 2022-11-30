package de.turtle_exception.client.api.request.entities;

import com.google.gson.JsonElement;
import de.turtle_exception.client.api.entities.JsonResource;
import de.turtle_exception.client.api.request.Action;

public interface JsonResourceAction extends Action<JsonResource> {
    JsonResourceAction setIdentifier(String identifier);

    JsonResourceAction setContent(JsonElement content);

    JsonResourceAction setEphemeral(Boolean b);
}
