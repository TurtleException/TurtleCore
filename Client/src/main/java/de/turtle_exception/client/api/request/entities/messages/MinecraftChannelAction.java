package de.turtle_exception.client.api.request.entities.messages;

import de.turtle_exception.client.api.entities.messages.MinecraftChannel;
import de.turtle_exception.client.api.entities.messages.SyncChannel;
import de.turtle_exception.client.api.request.Action;

public interface MinecraftChannelAction extends Action<MinecraftChannel> {
    MinecraftChannelAction setSyncChannel(SyncChannel syncChannel);

    MinecraftChannelAction setType(MinecraftChannel.Type type);

    MinecraftChannelAction setIdentifier(String identifier);
}
