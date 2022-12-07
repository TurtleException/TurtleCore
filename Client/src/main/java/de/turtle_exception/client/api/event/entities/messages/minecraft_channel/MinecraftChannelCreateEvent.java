package de.turtle_exception.client.api.event.entities.messages.minecraft_channel;

import de.turtle_exception.client.api.entities.messages.MinecraftChannel;
import de.turtle_exception.client.api.event.entities.EntityCreateEvent;
import org.jetbrains.annotations.NotNull;

public class MinecraftChannelCreateEvent extends MinecraftChannelEvent implements EntityCreateEvent<MinecraftChannel> {
    public MinecraftChannelCreateEvent(@NotNull MinecraftChannel minecraftChannel) {
        super(minecraftChannel);
    }
}
