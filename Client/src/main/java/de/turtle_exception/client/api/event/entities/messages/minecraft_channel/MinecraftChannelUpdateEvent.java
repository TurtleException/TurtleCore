package de.turtle_exception.client.api.event.entities.messages.minecraft_channel;

import de.turtle_exception.client.api.entities.messages.MinecraftChannel;
import de.turtle_exception.client.api.event.entities.EntityUpdateEvent;
import org.jetbrains.annotations.NotNull;

public abstract class MinecraftChannelUpdateEvent<V> extends MinecraftChannelEvent implements EntityUpdateEvent<MinecraftChannel, V> {
    private final @NotNull String key;

    protected final V oldValue;
    protected final V newValue;

    public MinecraftChannelUpdateEvent(@NotNull MinecraftChannel minecraftChannel, @NotNull String key, V oldValue, V newValue) {
        super(minecraftChannel);
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
