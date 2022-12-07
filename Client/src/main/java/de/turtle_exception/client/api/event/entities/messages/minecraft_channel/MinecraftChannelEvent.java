package de.turtle_exception.client.api.event.entities.messages.minecraft_channel;

import de.turtle_exception.client.api.entities.messages.MinecraftChannel;
import de.turtle_exception.client.api.event.entities.EntityEvent;
import org.jetbrains.annotations.NotNull;

public abstract class MinecraftChannelEvent extends EntityEvent<MinecraftChannel> {
    public MinecraftChannelEvent(@NotNull MinecraftChannel minecraftChannel) {
        super(minecraftChannel);
    }

    public @NotNull MinecraftChannel getMinecraftChannel() {
        return this.getEntity();
    }
}
