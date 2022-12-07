package de.turtle_exception.client.internal.entities.messages;

import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.messages.IChannel;
import de.turtle_exception.client.internal.entities.TurtleImpl;
import org.jetbrains.annotations.NotNull;

public abstract class ChannelImpl extends TurtleImpl implements IChannel {
    protected long syncChannel;

    protected ChannelImpl(@NotNull TurtleClient client, long id, long syncChannel) {
        super(client, id);

        this.syncChannel = syncChannel;
    }

    @Override
    public long getSyncChannelId() {
        return syncChannel;
    }
}
