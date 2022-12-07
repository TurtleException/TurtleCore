package de.turtle_exception.client.api.event.entities.messages.minecraft_channel;

import de.turtle_exception.client.api.entities.messages.MinecraftChannel;
import de.turtle_exception.client.internal.data.annotations.Keys;
import org.jetbrains.annotations.NotNull;

public class MinecraftChannelUpdateTypeEvent extends MinecraftChannelUpdateEvent<MinecraftChannel.Type> {
    public MinecraftChannelUpdateTypeEvent(@NotNull MinecraftChannel minecraftChannel, MinecraftChannel.Type oldValue, MinecraftChannel.Type newValue) {
        super(minecraftChannel, Keys.Messages.MinecraftChannel.TYPE, oldValue, newValue);
    }
}
