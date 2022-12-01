package de.turtle_exception.client.api.event.entities.messages.minecraft_channel;

import de.turtle_exception.client.api.entities.messages.MinecraftChannel;
import de.turtle_exception.client.api.entities.messages.SyncChannel;
import de.turtle_exception.client.internal.data.annotations.Keys;
import org.jetbrains.annotations.NotNull;

public class MinecraftChannelUpdateSyncChannelEvent extends MinecraftChannelUpdateEvent<SyncChannel> {
    public MinecraftChannelUpdateSyncChannelEvent(@NotNull MinecraftChannel minecraftChannel, SyncChannel oldValue, SyncChannel newValue) {
        super(minecraftChannel, Keys.Messages.IChannel.SYNC_CHANNEL, oldValue, newValue);
    }
}
