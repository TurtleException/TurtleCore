package de.turtle_exception.client.api.event.entities.messages.sync_message;

import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.entities.messages.SyncMessage;
import de.turtle_exception.client.internal.data.annotations.Keys;
import org.jetbrains.annotations.NotNull;

public class SyncMessageUpdateContentEvent extends SyncMessageUpdateEvent<String> {
    public SyncMessageUpdateContentEvent(@NotNull SyncMessage message, String oldValue, String newValue) {
        super(message, Keys.Messages.SyncMessage.CONTENT, oldValue, newValue);
    }
}
