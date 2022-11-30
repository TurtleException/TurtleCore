package de.turtle_exception.client.internal.request.actions.entities;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.turtle_exception.client.api.entities.JsonResource;
import de.turtle_exception.client.api.request.entities.JsonResourceAction;
import de.turtle_exception.client.internal.Provider;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.internal.request.actions.EntityAction;
import de.turtle_exception.client.internal.util.Checks;
import org.jetbrains.annotations.NotNull;

public class JsonResourceActionImpl extends EntityAction<JsonResource> implements JsonResourceAction {
    protected String identifier;
    protected JsonElement jsonContent;
    protected Boolean ephemeral;

    @SuppressWarnings("CodeBlock2Expr")
    public JsonResourceActionImpl(@NotNull Provider provider) {
        super(provider, JsonResource.class);

        this.checks.add(json -> { json.get(Keys.JsonResource.IDENTIFIER).getAsString(); });
        this.checks.add(json -> { Checks.nonNull(json.get(Keys.JsonResource.CONTENT)); });
        this.checks.add(json -> { json.get(Keys.JsonResource.EPHEMERAL).getAsBoolean(); });
    }

    @Override
    protected void updateContent() {
        this.content = new JsonObject();
        this.content.addProperty(Keys.JsonResource.IDENTIFIER, identifier);
        this.content.add(Keys.JsonResource.CONTENT, jsonContent);
        this.content.addProperty(Keys.JsonResource.EPHEMERAL, ephemeral);
    }

    /* - - - */

    @Override
    public JsonResourceAction setIdentifier(String identifier) {
        this.identifier = identifier;
        return this;
    }

    @Override
    public JsonResourceAction setContent(JsonElement content) {
        this.jsonContent = content;
        return this;
    }

    @Override
    public JsonResourceAction setEphemeral(Boolean b) {
        this.ephemeral = b;
        return this;
    }
}
