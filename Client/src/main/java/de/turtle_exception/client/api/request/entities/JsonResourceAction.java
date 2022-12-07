package de.turtle_exception.client.api.request.entities;

import com.google.gson.JsonElement;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.JsonResource;
import de.turtle_exception.client.api.request.Action;

import java.util.Collection;

/**
 * A JsonResourceAction is an Action that requests the creation of a new {@link JsonResource}, according to the
 * arguments this Action holds. If any required field is missing the server will reject the request and respond with an
 * error. Required fields are all attributes that are not a subclass of {@link Collection}, as these are set to an empty
 * Collection by default.
 * @see TurtleClient#createJsonResource()
 */
@SuppressWarnings("unused")
public interface JsonResourceAction extends Action<JsonResource> {
    /**
     * Sets the identifier of this JsonResource to the provided String.
     * @param identifier JsonResource identifier.
     * @return This JsonResourceAction for chaining convenience.
     */
    JsonResourceAction setIdentifier(String identifier);

    /**
     * Sets the content of this JsonResource to the provided JsonElement.
     * @param content JsonResource content.
     * @return This JsonResourceAction for chaining convenience.
     */
    JsonResourceAction setContent(JsonElement content);

    /**
     * Sets this JsonResource to be ephemeral.
     * @return This JsonResourceAction for chaining convenience.
     */
    JsonResourceAction setEphemeral();
}
