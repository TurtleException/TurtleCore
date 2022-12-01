package de.turtle_exception.client.api.event.entities.messages.sync_message;

import de.turtle_exception.client.api.entities.messages.SyncMessage;
import de.turtle_exception.client.api.event.entities.EntityDeleteEvent;
import org.jetbrains.annotations.NotNull;

public class SyncMessageDeleteEvent extends SyncMessageEvent implements EntityDeleteEvent<SyncMessage> {
    public SyncMessageDeleteEvent(@NotNull SyncMessage message) {
        super(message);
    }
}
