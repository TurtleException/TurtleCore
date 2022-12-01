package de.turtle_exception.client.api.event.entities.messages.sync_message;

import de.turtle_exception.client.api.entities.messages.IChannel;
import de.turtle_exception.client.api.entities.messages.SyncChannel;
import de.turtle_exception.client.api.entities.messages.SyncMessage;
import de.turtle_exception.client.internal.data.annotations.Keys;
import org.jetbrains.annotations.NotNull;

public class SyncMessageUpdateSourceEvent extends SyncMessageUpdateEvent<IChannel> {
    public SyncMessageUpdateSourceEvent(@NotNull SyncMessage message, IChannel oldValue, IChannel newValue) {
        super(message, Keys.Messages.SyncMessage.SOURCE, oldValue, newValue);
    }
}
