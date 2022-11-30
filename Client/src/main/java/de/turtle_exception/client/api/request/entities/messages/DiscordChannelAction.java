package de.turtle_exception.client.api.request.entities.messages;

import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.messages.DiscordChannel;
import de.turtle_exception.client.api.entities.messages.SyncChannel;
import de.turtle_exception.client.api.request.Action;

import java.util.Collection;

/**
 * A DiscordChannelAction is an Action that requests the creation of a new {@link DiscordChannel}, according to the
 * arguments this Action holds. If any required field is missing the server will reject the request and respond with an
 * error. Required fields are all attributes that are not a subclass of {@link Collection}, as these are set to an empty
 * Collection by default.
 * @see TurtleClient#createDiscordChannel()
 */
@SuppressWarnings("unused")
public interface DiscordChannelAction extends Action<DiscordChannel> {
    /**
     * Sets the SyncChannel of this DiscordChannel to the provided Channel.
     * @param syncChannel Parent SyncChannel.
     * @return This DiscordChannelAction for chaining convenience.
     */
    DiscordChannelAction setSyncChannel(SyncChannel syncChannel);

    /**
     * Sets the snowflake of this DiscordChannel to the provided {@code long}.
     * @param snowflake DiscordChannel snowflake.
     * @return This DiscordChannelAction for chaining convenience.
     */
    DiscordChannelAction setSnowflake(Long snowflake);
}
