package de.turtle_exception.client.internal.entities.messages;

import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.messages.DiscordChannel;
import de.turtle_exception.client.api.entities.messages.SyncMessage;
import de.turtle_exception.client.api.event.entities.messages.discord_channel.DiscordChannelUpdateSnowflakeEvent;
import de.turtle_exception.client.api.event.entities.messages.discord_channel.DiscordChannelUpdateSyncChannelEvent;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.internal.entities.TurtleImpl;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;

public class DiscordChannelImpl extends ChannelImpl implements DiscordChannel {
    private long snowflake;

    public DiscordChannelImpl(@NotNull TurtleClient client, long id, long syncChannel, long snowflake) {
        super(client, id, syncChannel);

        this.snowflake   = snowflake;
    }

    @Override
    public @NotNull TurtleImpl handleUpdate(@NotNull JsonObject json) {
        this.apply(json, Keys.Messages.IChannel.SYNC_CHANNEL, element -> {
            long old = this.syncChannel;
            this.syncChannel = element.getAsLong();
            this.fireEvent(new DiscordChannelUpdateSyncChannelEvent(this, old, this.syncChannel));
        });
        this.apply(json, Keys.Messages.DiscordChannel.SNOWFLAKE, element -> {
            long old = this.snowflake;
            this.snowflake = element.getAsLong();
            this.fireEvent(new DiscordChannelUpdateSnowflakeEvent(this, old, this.snowflake));
        });
        return this;
    }

    /* - - - */

    @Override
    public void receive(@NotNull Message msg) {
        // TODO
    }

    @Override
    public void send(@NotNull SyncMessage msg) {
        // TODO
    }

    /* - SYNC_CHANNEL - */

    @Override
    public @NotNull Action<DiscordChannel> modifySyncChannel(long syncChannel) {
        return getClient().getProvider().patch(this, Keys.Messages.IChannel.SYNC_CHANNEL, syncChannel).andThenParse(DiscordChannel.class);
    }

    /* - SNOWFLAKE - */

    @Override
    public long getSnowflake() {
        return this.snowflake;
    }

    @Override
    public @NotNull Action<DiscordChannel> modifySnowflake(long snowflake) {
        return getClient().getProvider().patch(this, Keys.Messages.DiscordChannel.SNOWFLAKE, snowflake).andThenParse(DiscordChannel.class);
    }
}
