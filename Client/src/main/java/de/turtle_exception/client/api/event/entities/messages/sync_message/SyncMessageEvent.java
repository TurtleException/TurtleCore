package de.turtle_exception.client.api.event.entities.messages.sync_message;

import de.turtle_exception.client.api.entities.messages.SyncMessage;
import de.turtle_exception.client.api.event.entities.EntityEvent;
import org.jetbrains.annotations.NotNull;

public abstract class SyncMessageEvent extends EntityEvent<SyncMessage> {
    public SyncMessageEvent(@NotNull SyncMessage message) {
        super(message);
    }

    public @NotNull SyncMessage getMessage() {
        return this.getEntity();
    }
}
