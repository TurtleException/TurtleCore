package de.turtle_exception.client.internal.request.actions.entities;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.turtle_exception.client.api.entities.JsonResource;
import de.turtle_exception.client.api.request.entities.JsonResourceAction;
import de.turtle_exception.client.internal.Provider;
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

        this.checks.add(json -> { json.get("identifier").getAsString(); });
        this.checks.add(json -> { Checks.nonNull(json.get("content")); });
        this.checks.add(json -> { json.get("ephemeral").getAsBoolean(); });
    }

    @Override
    protected void updateContent() {
        this.content = new JsonObject();
        this.content.addProperty("identifier", identifier);
        this.content.add("content", jsonContent);
        this.content.addProperty("ephemeral", ephemeral);
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
