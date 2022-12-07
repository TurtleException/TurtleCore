package de.turtle_exception.client.api.event.entities.messages.sync_message;

import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.entities.messages.SyncMessage;
import de.turtle_exception.client.api.event.entities.EntityUpdateEntryEvent;
import de.turtle_exception.client.internal.data.annotations.Keys;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Function;

public class SyncMessageUpdateAuthorEvent extends SyncMessageUpdateEvent<User> {
    public SyncMessageUpdateAuthorEvent(@NotNull SyncMessage message, User oldValue, User newValue) {
        super(message, Keys.Messages.SyncMessage.AUTHOR, oldValue, newValue);
    }
}
