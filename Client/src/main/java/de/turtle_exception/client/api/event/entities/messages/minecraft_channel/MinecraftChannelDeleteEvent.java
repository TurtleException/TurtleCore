package de.turtle_exception.client.api.event.entities.messages.minecraft_channel;

import de.turtle_exception.client.api.entities.messages.MinecraftChannel;
import de.turtle_exception.client.api.event.entities.EntityDeleteEvent;
import org.jetbrains.annotations.NotNull;

public class MinecraftChannelDeleteEvent extends MinecraftChannelEvent implements EntityDeleteEvent<MinecraftChannel> {
    public MinecraftChannelDeleteEvent(@NotNull MinecraftChannel minecraftChannel) {
        super(minecraftChannel);
    }
}
