package de.turtle_exception.client.api.event.entities.messages.sync_message;

import de.turtle_exception.client.api.entities.messages.SyncMessage;
import de.turtle_exception.client.api.event.entities.EntityCreateEvent;
import org.jetbrains.annotations.NotNull;

public class SyncMessageCreateEvent extends SyncMessageEvent implements EntityCreateEvent<SyncMessage> {
    public SyncMessageCreateEvent(@NotNull SyncMessage message) {
        super(message);
    }
}
