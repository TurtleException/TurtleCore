package de.turtle_exception.client.api.entities.messages;

import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.data.annotations.Key;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.internal.data.annotations.Relation;
import de.turtle_exception.client.internal.data.annotations.Types;
import org.jetbrains.annotations.NotNull;

/** An application-specific channel. This is not a resource itself, but all implementations must be resources. */
@SuppressWarnings("unused")
public interface IChannel extends Turtle {
    @Override
    @NotNull Action<? extends IChannel> update();

    // TODO: circular dependency
    @Key(name = Keys.Messages.IChannel.SYNC_CHANNEL, relation = Relation.MANY_TO_ONE, sqlType = Types.Messages.IChannel.SYNC_CHANNEL)
    @NotNull SyncChannel getSyncChannel();

    // TODO
    void send(@NotNull SyncMessage msg);
}
