package de.turtle_exception.client.api.event.entities.messages.minecraft_channel;

import de.turtle_exception.client.api.entities.messages.MinecraftChannel;
import de.turtle_exception.client.internal.data.annotations.Keys;
import org.jetbrains.annotations.NotNull;

public class MinecraftChannelUpdateSyncChannelEvent extends MinecraftChannelUpdateEvent<Long> {
    public MinecraftChannelUpdateSyncChannelEvent(@NotNull MinecraftChannel minecraftChannel, long oldValue, long newValue) {
        super(minecraftChannel, Keys.Messages.IChannel.SYNC_CHANNEL, oldValue, newValue);
    }
}
