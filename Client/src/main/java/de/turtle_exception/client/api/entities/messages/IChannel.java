package de.turtle_exception.client.api.entities.messages;

import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.internal.data.annotations.Key;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.internal.data.annotations.Relation;
import de.turtle_exception.client.internal.data.annotations.Types;
import org.jetbrains.annotations.NotNull;

public interface IChannel extends Turtle {
    @Key(name = Keys.Messages.IChannel.SYNC_CHANNEL, relation = Relation.MANY_TO_ONE, sqlType = Types.Messages.IChannel.SYNC_CHANNEL)
    @NotNull SyncChannel getSyncChannel();

    // TODO
    void send(@NotNull SyncMessage msg);
}
