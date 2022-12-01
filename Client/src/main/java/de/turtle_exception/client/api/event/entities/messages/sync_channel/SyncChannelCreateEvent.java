package de.turtle_exception.client.api.event.entities.messages.sync_channel;

import de.turtle_exception.client.api.entities.messages.SyncChannel;
import de.turtle_exception.client.api.event.entities.EntityCreateEvent;
import org.jetbrains.annotations.NotNull;

public class SyncChannelCreateEvent extends SyncChannelEvent implements EntityCreateEvent<SyncChannel> {
    public SyncChannelCreateEvent(@NotNull SyncChannel channel) {
        super(channel);
    }
}
