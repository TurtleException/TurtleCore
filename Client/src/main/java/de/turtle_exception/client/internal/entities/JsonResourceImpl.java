package de.turtle_exception.client.internal.entities;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.JsonResource;
import de.turtle_exception.client.api.event.entities.json_resource.JsonResourceUpdateContentEvent;
import de.turtle_exception.client.api.event.entities.json_resource.JsonResourceUpdateEphemeralEvent;
import de.turtle_exception.client.api.event.entities.json_resource.JsonResourceUpdateIdentifierEvent;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.data.annotations.Keys;
import org.jetbrains.annotations.NotNull;

public class JsonResourceImpl extends TurtleImpl implements JsonResource {
    private String      identifier;
    private JsonElement content;
    private boolean     ephemeral;

    protected JsonResourceImpl(@NotNull TurtleClient client, long id, @NotNull String identifier, @NotNull JsonElement content, boolean ephemeral) {
        super(client, id);

        this.identifier = identifier;
        this.content    = content;
        this.ephemeral  = ephemeral;
    }

    @Override
    public @NotNull JsonResourceImpl handleUpdate(@NotNull JsonObject json) {
        this.apply(json, Keys.JsonResource.IDENTIFIER, element -> {
            String old = this.identifier;
            this.identifier = element.getAsString();
            this.fireEvent(new JsonResourceUpdateIdentifierEvent(this, old, this.identifier));
        });
        this.apply(json, Keys.JsonResource.CONTENT, element -> {
            JsonElement old = this.content;
            this.content = element;
            this.fireEvent(new JsonResourceUpdateContentEvent(this, old, this.content));
        });
        this.apply(json, Keys.Attribute.EphemeralType.EPHEMERAL, element -> {
            boolean old = this.ephemeral;
            this.ephemeral = element.getAsBoolean();
            this.fireEvent(new JsonResourceUpdateEphemeralEvent(this, old, this.ephemeral));
        });
        return this;
    }

    /* - IDENTIFIER - */

    @Override
    public @NotNull String getIdentifier() {
        return this.identifier;
    }

    @Override
    public @NotNull Action<JsonResource> modifyIdentifier(@NotNull String identifier) {
        return getClient().getProvider().patch(this, Keys.JsonResource.IDENTIFIER, identifier).andThenParse(JsonResource.class);
    }

    /* - CONTENT - */

    @Override
    public @NotNull JsonElement getContent() {
        return this.content;
    }

    @Override
    public @NotNull Action<JsonResource> modifyContent(@NotNull JsonElement content) {
        return getClient().getProvider().patch(this, Keys.JsonResource.CONTENT, content).andThenParse(JsonResource.class);
    }

    /* - EPHEMERAL - */

    @Override
    public boolean isEphemeral() {
        return this.ephemeral;
    }

    @Override
    public @NotNull Action<JsonResource> modifyEphemeral(boolean ephemeral) {
        return getClient().getProvider().patch(this, Keys.Attribute.EphemeralType.EPHEMERAL, ephemeral).andThenParse(JsonResource.class);
    }
}
