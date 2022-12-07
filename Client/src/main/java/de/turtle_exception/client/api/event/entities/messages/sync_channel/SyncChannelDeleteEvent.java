package de.turtle_exception.client.api.event.entities.messages.sync_channel;

import de.turtle_exception.client.api.entities.messages.SyncChannel;
import de.turtle_exception.client.api.event.entities.EntityDeleteEvent;
import org.jetbrains.annotations.NotNull;

public class SyncChannelDeleteEvent extends SyncChannelEvent implements EntityDeleteEvent<SyncChannel> {
    public SyncChannelDeleteEvent(@NotNull SyncChannel channel) {
        super(channel);
    }
}
