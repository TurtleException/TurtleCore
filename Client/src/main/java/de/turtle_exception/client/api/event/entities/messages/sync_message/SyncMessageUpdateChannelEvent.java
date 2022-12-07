package de.turtle_exception.client.api.event.entities.messages.sync_message;

import de.turtle_exception.client.api.entities.messages.SyncChannel;
import de.turtle_exception.client.api.entities.messages.SyncMessage;
import de.turtle_exception.client.internal.data.annotations.Keys;
import org.jetbrains.annotations.NotNull;

public class SyncMessageUpdateChannelEvent extends SyncMessageUpdateEvent<Long> {
    public SyncMessageUpdateChannelEvent(@NotNull SyncMessage message, Long oldValue, Long newValue) {
        super(message, Keys.Messages.SyncMessage.CHANNEL, oldValue, newValue);
    }

    public SyncChannel getOldChannel() {
        return this.getClient().getTurtleById(this.getOldValue(), SyncChannel.class);
    }

    public SyncChannel getNewChannel() {
        return this.getClient().getTurtleById(this.getNewValue(), SyncChannel.class);
    }
}
