package de.turtle_exception.client.api.event.entities.messages.sync_channel;

import de.turtle_exception.client.api.entities.messages.MinecraftChannel;
import de.turtle_exception.client.api.entities.messages.SyncChannel;
import de.turtle_exception.client.api.event.entities.EntityUpdateEntryEvent;
import de.turtle_exception.client.internal.data.annotations.Keys;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Function;

public class SyncChannelMinecraftChannelAddEvent extends SyncChannelEvent implements EntityUpdateEntryEvent<SyncChannel, MinecraftChannel> {
    protected final MinecraftChannel minecraftChannel;

    public SyncChannelMinecraftChannelAddEvent(@NotNull SyncChannel channel, @NotNull MinecraftChannel minecraftChannel) {
        super(channel);
        this.minecraftChannel = minecraftChannel;
    }

    public @NotNull MinecraftChannel getMinecraftChannel() {
        return minecraftChannel;
    }

    /* - EntityUpdateEntryEvent - */

    @Override
    public @NotNull String getKey() {
        return Keys.Messages.SyncChannel.MINECRAFT;
    }

    @Override
    public @NotNull Collection<MinecraftChannel> getCollection() {
        return getChannel().getMinecraftChannels();
    }

    @Override
    public @NotNull Function<MinecraftChannel, Object> getMutator() {
        return MinecraftChannel::getId;
    }
}
