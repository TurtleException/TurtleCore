package de.turtle_exception.client.api.request.entities.messages;

import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.messages.MinecraftChannel;
import de.turtle_exception.client.api.entities.messages.SyncChannel;
import de.turtle_exception.client.api.request.Action;

import java.util.Collection;

/**
 * A MinecraftChannelAction is an Action that requests the creation of a new {@link MinecraftChannel}, according to the
 * arguments this Action holds. If any required field is missing the server will reject the request and respond with an
 * error. Required fields are all attributes that are not a subclass of {@link Collection}, as these are set to an empty
 * Collection by default.
 * @see TurtleClient#createMinecraftChannel()
 */
@SuppressWarnings("unused")
public interface MinecraftChannelAction extends Action<MinecraftChannel> {
    /**
     * Sets the SyncChannel of this MinecraftChannel to the provided Channel.
     * @param syncChannel Parent SyncChannel.
     * @return This MinecraftChannelAction for chaining convenience.
     */
    MinecraftChannelAction setSyncChannel(SyncChannel syncChannel);

    /**
     * Sets the type of this MinecraftChannel to the provided Type.
     * @param type MinecraftChannel type.
     * @return This MinecraftChannelAction for chaining convenience.
     */
    MinecraftChannelAction setType(MinecraftChannel.Type type);

    /**
     * Sets the identifier of this MinecraftChannel to the provided String.
     * @param identifier MinecraftChannel identifier.
     * @return This MinecraftChannelAction for chaining convenience.
     */
    MinecraftChannelAction setIdentifier(String identifier);
}
