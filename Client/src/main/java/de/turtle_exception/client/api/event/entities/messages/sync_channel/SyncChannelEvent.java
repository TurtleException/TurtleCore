package de.turtle_exception.client.api.event.entities.messages.sync_channel;

import de.turtle_exception.client.api.entities.messages.SyncChannel;
import de.turtle_exception.client.api.event.entities.EntityEvent;
import org.jetbrains.annotations.NotNull;

public abstract class SyncChannelEvent extends EntityEvent<SyncChannel> {
    public SyncChannelEvent(@NotNull SyncChannel channel) {
        super(channel);
    }

    public @NotNull SyncChannel getChannel() {
        return this.getEntity();
    }
}
