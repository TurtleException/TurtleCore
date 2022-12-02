package de.turtle_exception.client.api.entities;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.data.annotations.Key;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.internal.data.annotations.Resource;
import de.turtle_exception.client.internal.data.annotations.Types;
import org.jetbrains.annotations.NotNull;

/**
 * A simple data structure to store a {@link JsonElement}.
 * <p> This resource is mainly intended to be used in newer client-versions, in case the server is not updated yet.
 */
@Resource(path = "json_resources", builder = "buildJsonResource")
@SuppressWarnings("unused")
public interface JsonResource extends Turtle {
    @Override
    default @NotNull Action<JsonResource> update() {
        return this.getClient().retrieveJsonResource(this.getId());
    }

    /**
     * Returns the String identifier of this JsonResource. Meaning, a hint for intended recipients as to how this
     * JsonResource should be handled / parsed.
     * <p> The identifier should be as unique as possible to avoid collisions. This also means that implementations
     * parsing this JsonResource should be prepared for possible identifier collisions by properly handling exceptions.
     * @return String identifying the nature of this JsonResource.
     */
    @Key(name = Keys.JsonResource.IDENTIFIER, sqlType = Types.JsonResource.IDENTIFIER)
    @NotNull String getIdentifier();

    @NotNull Action<JsonResource> modifyIdentifier(@NotNull String identifier);

    /**
     * Provides the underlying data in form of a {@link JsonElement}. This would probably be a {@link JsonObject} or
     * {@link JsonArray} for most use-cases of this resource.
     * @return The underlying JSON data.
     */
    @Key(name = Keys.JsonResource.CONTENT, sqlType = Types.JsonResource.CONTENT)
    @NotNull JsonElement getContent();

    @NotNull Action<JsonResource> modifyContent(@NotNull JsonElement content);

    // TODO: update this when the event-thing is implemented
    /**
     * Returns true if this resource is ephemeral.
     * <p> When a JsonResource is ephemeral it will not be cached by clients (at least not internally) or the server. It
     * will however, fire an event so that it can be processed once.
     * <p> This might be useful for resources that are used often, but don't need to be logged - especially if they
     * would amount to great sums of unnecessarily complex data.
     * @return {@code true} if this resource is ephemeral.
     */
    @Key(name = Keys.JsonResource.EPHEMERAL, sqlType = Types.JsonResource.EPHEMERAL)
    boolean isEphemeral();

    @NotNull Action<JsonResource> modifyEphemeral(boolean ephemeral);
}
