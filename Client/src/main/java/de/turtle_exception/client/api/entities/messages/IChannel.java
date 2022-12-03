package de.turtle_exception.client.api.entities.messages;

import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.data.annotations.*;
import org.jetbrains.annotations.NotNull;

/** An application-specific channel. This is not a resource itself, but all implementations must be resources. */
@SuppressWarnings("unused")
public interface IChannel extends Turtle {
    @Override
    @NotNull Action<? extends IChannel> update();

    @Key(name = Keys.Messages.IChannel.SYNC_CHANNEL, sqlType = Types.Messages.IChannel.SYNC_CHANNEL)
    long getSyncChannelId();

    default SyncChannel getSyncChannel() {
        return getClient().getTurtleById(this.getSyncChannelId(), SyncChannel.class);
    }

    @NotNull Action<? extends IChannel> modifySyncChannel(long syncChannel);

    default @NotNull Action<? extends IChannel> modifySyncChannel(@NotNull SyncChannel channel) {
        return this.modifySyncChannel(channel.getId());
    }

    // TODO
    void send(@NotNull SyncMessage msg);
}
