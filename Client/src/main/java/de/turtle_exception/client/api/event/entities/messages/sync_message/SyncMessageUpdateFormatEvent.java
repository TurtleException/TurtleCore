package de.turtle_exception.client.api.event.entities.messages.sync_message;

import de.turtle_exception.client.api.entities.attributes.MessageFormat;
import de.turtle_exception.client.api.entities.messages.SyncMessage;
import de.turtle_exception.client.internal.data.annotations.Keys;
import org.jetbrains.annotations.NotNull;

public class SyncMessageUpdateFormatEvent extends SyncMessageUpdateEvent<MessageFormat> {
    public SyncMessageUpdateFormatEvent(@NotNull SyncMessage message, MessageFormat oldValue, MessageFormat newValue) {
        super(message, Keys.Messages.SyncMessage.FORMAT, oldValue, newValue);
    }
}
