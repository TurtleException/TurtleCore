package de.turtle_exception.client.api.entities.messages;

import de.turtle_exception.client.internal.data.annotations.Key;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.internal.data.annotations.Resource;
import de.turtle_exception.client.internal.data.annotations.Types;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;

@Resource(path = "discord_channels", builder = "buildDiscordChannel")
@SuppressWarnings("unused")
public interface DiscordChannel extends IChannel {
    @Key(name = Keys.Messages.DiscordChannel.SNOWFLAKE, sqlType = Types.Messages.DiscordChannel.SNOWFLAKE)
    long getSnowflake();

    void receive(@NotNull Message msg);
}
