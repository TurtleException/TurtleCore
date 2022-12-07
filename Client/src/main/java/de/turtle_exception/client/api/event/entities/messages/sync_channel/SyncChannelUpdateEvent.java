package de.turtle_exception.client.api.event.entities.messages.sync_channel;

import de.turtle_exception.client.api.entities.messages.SyncChannel;
import de.turtle_exception.client.api.event.entities.EntityUpdateEvent;
import org.jetbrains.annotations.NotNull;

// not needed yet
@SuppressWarnings("unused")
public abstract class SyncChannelUpdateEvent<V> extends SyncChannelEvent implements EntityUpdateEvent<SyncChannel, V> {
    private final @NotNull String key;

    protected final V oldValue;
    protected final V newValue;

    public SyncChannelUpdateEvent(@NotNull SyncChannel channel, @NotNull String key, V oldValue, V newValue) {
        super(channel);
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
