package de.turtle_exception.client.api.event.entities.messages.sync_channel;

import de.turtle_exception.client.api.entities.messages.DiscordChannel;
import de.turtle_exception.client.api.entities.messages.SyncChannel;
import de.turtle_exception.client.api.event.entities.EntityUpdateEntryEvent;
import de.turtle_exception.client.internal.data.annotations.Keys;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Function;

public class SyncChannelDiscordChannelRemoveEvent extends SyncChannelEvent implements EntityUpdateEntryEvent<SyncChannel, DiscordChannel> {
    protected final DiscordChannel discordChannel;

    public SyncChannelDiscordChannelRemoveEvent(@NotNull SyncChannel channel, @NotNull DiscordChannel discordChannel) {
        super(channel);
        this.discordChannel = discordChannel;
    }

    public @NotNull DiscordChannel getDiscordChannel() {
        return discordChannel;
    }

    /* - EntityUpdateEntryEvent - */

    @Override
    public @NotNull String getKey() {
        return Keys.Messages.SyncChannel.DISCORD;
    }

    @Override
    public @NotNull Collection<DiscordChannel> getCollection() {
        return getChannel().getDiscordChannels();
    }

    @Override
    public @NotNull Function<DiscordChannel, Object> getMutator() {
        return DiscordChannel::getId;
    }
}
