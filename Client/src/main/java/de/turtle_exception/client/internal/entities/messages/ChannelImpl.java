package de.turtle_exception.client.internal.entities.messages;

import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.messages.IChannel;
import de.turtle_exception.client.api.entities.messages.SyncChannel;
import de.turtle_exception.client.internal.entities.TurtleImpl;
import org.jetbrains.annotations.NotNull;

public abstract class ChannelImpl extends TurtleImpl implements IChannel {
    protected SyncChannel syncChannel;

    protected ChannelImpl(@NotNull TurtleClient client, long id, SyncChannel syncChannel) {
        super(client, id);

        this.syncChannel = syncChannel;
    }

    @Override
    public @NotNull SyncChannel getSyncChannel() {
        return this.syncChannel;
    }
}
