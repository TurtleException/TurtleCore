package de.turtle_exception.client.api.entities.messages;

import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.data.annotations.Key;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.internal.data.annotations.Resource;
import de.turtle_exception.client.internal.data.annotations.Types;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;

@Resource(path = "discord_channels", builder = "buildDiscordChannel")
@SuppressWarnings("unused")
public interface DiscordChannel extends IChannel {
    @Override
    default @NotNull Action<DiscordChannel> update() {
        return this.getClient().retrieveDiscordChannel(this.getId());
    }

    @Override
    @NotNull Action<DiscordChannel> modifySyncChannel(long syncChannel);

    @Override
    @NotNull
    default Action<DiscordChannel> modifySyncChannel(@NotNull SyncChannel channel) {
        return this.modifySyncChannel(channel.getId());
    }

    @Key(name = Keys.Messages.DiscordChannel.SNOWFLAKE, sqlType = Types.Messages.DiscordChannel.SNOWFLAKE)
    long getSnowflake();

    @NotNull Action<DiscordChannel> modifySnowflake(long snowflake);

    void receive(@NotNull Message msg);
}
