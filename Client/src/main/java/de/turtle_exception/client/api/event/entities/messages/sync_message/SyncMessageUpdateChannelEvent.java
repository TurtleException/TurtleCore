package de.turtle_exception.client.api.event.entities.messages.sync_message;

import de.turtle_exception.client.api.entities.messages.SyncChannel;
import de.turtle_exception.client.api.entities.messages.SyncMessage;
import de.turtle_exception.client.internal.data.annotations.Keys;
import org.jetbrains.annotations.NotNull;

public class SyncMessageUpdateChannelEvent extends SyncMessageUpdateEvent<SyncChannel> {
    public SyncMessageUpdateChannelEvent(@NotNull SyncMessage message, SyncChannel oldValue, SyncChannel newValue) {
        super(message, Keys.Messages.SyncMessage.CHANNEL, oldValue, newValue);
    }
}
