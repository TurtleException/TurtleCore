package de.turtle_exception.client.api.entities.messages;

import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.data.annotations.*;
import org.jetbrains.annotations.NotNull;

/**
 * An application-specific channel. This is not a resource itself, but all implementations must be resources.
 * @see DiscordChannel
 * @see MinecraftChannel
 */
@SuppressWarnings("unused")
public interface IChannel extends Turtle {
    @Override
    @NotNull Action<? extends IChannel> update();

    /* - SYNC CHANNEL - */

    /**
     * Provides the id of this Channel's {@link SyncChannel}.
     * @return The super SyncChannel id.
     */
    @Key(name = Keys.Messages.IChannel.SYNC_CHANNEL, sqlType = Types.Messages.IChannel.SYNC_CHANNEL)
    long getSyncChannelId();

    /**
     * Provides this Channel's {@link SyncChannel}.
     * @return The super SyncChannel.
     */
    default SyncChannel getSyncChannel() {
        return getClient().getTurtleById(this.getSyncChannelId(), SyncChannel.class);
    }

    /**
     * Creates an Action with the instruction to modify this Channel's super SyncChannel and change it to the provided id.
     * @param syncChannel New SyncChannel id.
     * @return Action that provides the modified {@link IChannel} or subclass on completion.
     */
    @NotNull Action<? extends IChannel> modifySyncChannel(long syncChannel);

    /**
     * Creates an Action with the instruction to modify this Channel's super SyncChannel and change it to the provided channel.
     * @param channel New SyncChannel.
     * @return Action that provides the modified {@link IChannel} or subclass on completion.
     */
    default @NotNull Action<? extends IChannel> modifySyncChannel(@NotNull SyncChannel channel) {
        return this.modifySyncChannel(channel.getId());
    }

    /* - LOGIC - */

    // TODO
    void send(@NotNull SyncMessage msg);
}
