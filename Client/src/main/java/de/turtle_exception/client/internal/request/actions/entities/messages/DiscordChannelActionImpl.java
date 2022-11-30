package de.turtle_exception.client.internal.request.actions.entities.messages;

import com.google.gson.JsonObject;
import de.turtle_exception.client.api.entities.messages.DiscordChannel;
import de.turtle_exception.client.api.entities.messages.SyncChannel;
import de.turtle_exception.client.api.request.entities.messages.DiscordChannelAction;
import de.turtle_exception.client.internal.Provider;
import de.turtle_exception.client.internal.request.actions.EntityAction;
import org.jetbrains.annotations.NotNull;

public class DiscordChannelActionImpl extends EntityAction<DiscordChannel> implements DiscordChannelAction {
    protected SyncChannel syncChannel;
    protected Long snowflake;

    public DiscordChannelActionImpl(@NotNull Provider provider) {
        super(provider, DiscordChannel.class);

        this.checks.add(json -> { json.get("channel").getAsLong(); });
        this.checks.add(json -> { json.get("snowflake").getAsLong(); });
    }

    @Override
    protected void updateContent() {
        this.content = new JsonObject();
        this.content.addProperty("channel", syncChannel.getId());
        this.content.addProperty("snowflake", snowflake);
    }

    /* - - - */

    @Override
    public DiscordChannelAction setSyncChannel(SyncChannel syncChannel) {
        this.syncChannel = syncChannel;
        return this;
    }

    @Override
    public DiscordChannelAction setSnowflake(Long snowflake) {
        this.snowflake = snowflake;
        return this;
    }
}
