package de.turtle_exception.client.api.event.entities.messages.discord_channel;

import de.turtle_exception.client.api.entities.messages.DiscordChannel;
import de.turtle_exception.client.internal.data.annotations.Keys;
import org.jetbrains.annotations.NotNull;

public class DiscordChannelUpdateSyncChannelEvent extends DiscordChannelUpdateEvent<Long> {
    public DiscordChannelUpdateSyncChannelEvent(@NotNull DiscordChannel discordChannel, long oldValue, long newValue) {
        super(discordChannel, Keys.Messages.IChannel.SYNC_CHANNEL, oldValue, newValue);
    }
}
