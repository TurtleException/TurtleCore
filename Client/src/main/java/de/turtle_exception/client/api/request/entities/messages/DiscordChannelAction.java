package de.turtle_exception.client.api.request.entities.messages;

import de.turtle_exception.client.api.entities.messages.DiscordChannel;
import de.turtle_exception.client.api.entities.messages.SyncChannel;
import de.turtle_exception.client.api.request.Action;

public interface DiscordChannelAction extends Action<DiscordChannel> {
    DiscordChannelAction setSyncChannel(SyncChannel syncChannel);

    DiscordChannelAction setSnowflake(Long snowflake);
}
