package de.turtle_exception.client.api.event.entities.messages.minecraft_channel;

import de.turtle_exception.client.api.entities.messages.MinecraftChannel;
import de.turtle_exception.client.internal.data.annotations.Keys;
import org.jetbrains.annotations.NotNull;

public class MinecraftChannelUpdateIdentifierEvent extends MinecraftChannelUpdateEvent<String> {
    public MinecraftChannelUpdateIdentifierEvent(@NotNull MinecraftChannel minecraftChannel, String oldValue, String newValue) {
        super(minecraftChannel, Keys.Messages.MinecraftChannel.IDENTIFIER, oldValue, newValue);
    }
}
