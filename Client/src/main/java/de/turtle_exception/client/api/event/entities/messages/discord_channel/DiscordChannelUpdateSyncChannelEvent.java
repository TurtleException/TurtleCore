package de.turtle_exception.client.api.event.entities.messages.discord_channel;

import de.turtle_exception.client.api.entities.messages.DiscordChannel;
import de.turtle_exception.client.api.entities.messages.SyncChannel;
import de.turtle_exception.client.internal.data.annotations.Keys;
import org.jetbrains.annotations.NotNull;

public class DiscordChannelUpdateSyncChannelEvent extends DiscordChannelUpdateEvent<SyncChannel> {
    public DiscordChannelUpdateSyncChannelEvent(@NotNull DiscordChannel discordChannel, SyncChannel oldValue, SyncChannel newValue) {
        super(discordChannel, Keys.Messages.IChannel.SYNC_CHANNEL, oldValue, newValue);
    }
}
