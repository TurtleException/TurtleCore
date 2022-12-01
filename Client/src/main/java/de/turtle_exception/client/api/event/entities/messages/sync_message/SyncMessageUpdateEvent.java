package de.turtle_exception.client.api.event.entities.messages.sync_message;

import de.turtle_exception.client.api.entities.messages.SyncChannel;
import de.turtle_exception.client.api.entities.messages.SyncMessage;
import de.turtle_exception.client.api.event.entities.EntityUpdateEvent;
import org.jetbrains.annotations.NotNull;

public class SyncMessageUpdateEvent<V> extends SyncMessageEvent implements EntityUpdateEvent<SyncMessage, V> {
    private final @NotNull String key;

    protected final V oldValue;
    protected final V newValue;

    public SyncMessageUpdateEvent(@NotNull SyncMessage message, @NotNull String key, V oldValue, V newValue) {
        super(message);
        this.key = key;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    @Override
    public final @NotNull String getKey() {
        return key;
    }

    @Override
    public V getOldValue() {
        return oldValue;
    }

    @Override
    public V getNewValue() {
        return newValue;
    }
}
