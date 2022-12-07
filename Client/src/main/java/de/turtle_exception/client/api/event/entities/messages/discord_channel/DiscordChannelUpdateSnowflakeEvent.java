package de.turtle_exception.client.api.event.entities.messages.discord_channel;

import de.turtle_exception.client.api.entities.messages.DiscordChannel;
import de.turtle_exception.client.internal.data.annotations.Keys;
import org.jetbrains.annotations.NotNull;

public class DiscordChannelUpdateSnowflakeEvent extends DiscordChannelUpdateEvent<Long> {
    public DiscordChannelUpdateSnowflakeEvent(@NotNull DiscordChannel discordChannel, Long oldValue, Long newValue) {
        super(discordChannel, Keys.Messages.DiscordChannel.SNOWFLAKE, oldValue, newValue);
    }
}
