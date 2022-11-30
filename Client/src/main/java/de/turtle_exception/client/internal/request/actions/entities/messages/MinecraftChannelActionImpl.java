package de.turtle_exception.client.internal.request.actions.entities.messages;

import com.google.gson.JsonObject;
import de.turtle_exception.client.api.entities.messages.MinecraftChannel;
import de.turtle_exception.client.api.entities.messages.SyncChannel;
import de.turtle_exception.client.api.request.entities.messages.MinecraftChannelAction;
import de.turtle_exception.client.internal.Provider;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.internal.request.actions.EntityAction;
import org.jetbrains.annotations.NotNull;

public class MinecraftChannelActionImpl extends EntityAction<MinecraftChannel> implements MinecraftChannelAction {
    protected SyncChannel syncChannel;
    protected Byte type;
    protected String identifier;

    public MinecraftChannelActionImpl(@NotNull Provider provider) {
        super(provider, MinecraftChannel.class);

        this.checks.add(json -> { json.get(Keys.Messages.IChannel.SYNC_CHANNEL).getAsLong(); });
        this.checks.add(json -> { json.get(Keys.Messages.MinecraftChannel.TYPE).getAsByte(); });
        this.checks.add(json -> { json.get(Keys.Messages.MinecraftChannel.IDENTIFIER).getAsString(); });
    }

    @Override
    protected void updateContent() {
        this.content = new JsonObject();
        this.content.addProperty(Keys.Messages.IChannel.SYNC_CHANNEL, syncChannel.getId());
        this.content.addProperty(Keys.Messages.MinecraftChannel.TYPE, type);
        this.content.addProperty(Keys.Messages.MinecraftChannel.IDENTIFIER, identifier);
    }

    /* - - - */

    @Override
    public MinecraftChannelAction setSyncChannel(SyncChannel syncChannel) {
        this.syncChannel = syncChannel;
        return this;
    }

    @Override
    public MinecraftChannelAction setType(MinecraftChannel.Type type) {
        this.type = type.code;
        return this;
    }

    @Override
    public MinecraftChannelAction setIdentifier(String identifier) {
        this.identifier = identifier;
        return this;
    }
}
