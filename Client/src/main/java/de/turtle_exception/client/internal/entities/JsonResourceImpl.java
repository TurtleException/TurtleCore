package de.turtle_exception.client.internal.entities;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.JsonResource;
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
        // TODO
        return null;
    }

    /* - - - */

    @Override
    public @NotNull String getIdentifier() {
        return this.identifier;
    }

    @Override
    public @NotNull JsonElement getContent() {
        return this.content;
    }

    @Override
    public boolean isEphemeral() {
        return this.ephemeral;
    }
}
