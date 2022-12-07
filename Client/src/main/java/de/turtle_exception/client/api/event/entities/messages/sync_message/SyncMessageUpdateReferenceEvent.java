package de.turtle_exception.client.api.event.entities.messages.sync_message;

import de.turtle_exception.client.api.entities.attributes.MessageFormat;
import de.turtle_exception.client.api.entities.messages.SyncMessage;
import de.turtle_exception.client.internal.data.annotations.Keys;
import org.jetbrains.annotations.NotNull;

public class SyncMessageUpdateReferenceEvent extends SyncMessageUpdateEvent<Long> {
    public SyncMessageUpdateReferenceEvent(@NotNull SyncMessage message, Long oldValue, Long newValue) {
        super(message, Keys.Messages.SyncMessage.REFERENCE, oldValue, newValue);
    }
}
