package de.turtle_exception.client.api.event.entities.messages.sync_message;

import de.turtle_exception.client.api.entities.messages.IChannel;
import de.turtle_exception.client.api.entities.messages.SyncChannel;
import de.turtle_exception.client.api.entities.messages.SyncMessage;
import de.turtle_exception.client.internal.data.annotations.Keys;
import org.jetbrains.annotations.NotNull;

public class SyncMessageUpdateSourceEvent extends SyncMessageUpdateEvent<Long> {
    public SyncMessageUpdateSourceEvent(@NotNull SyncMessage message, Long oldValue, Long newValue) {
        super(message, Keys.Messages.SyncMessage.SOURCE, oldValue, newValue);
    }

    public IChannel getOldChannel() {
        return this.getClient().getTurtleById(this.getOldValue(), IChannel.class);
    }

    public IChannel getNewChannel() {
        return this.getClient().getTurtleById(this.getNewValue(), IChannel.class);
    }
}
