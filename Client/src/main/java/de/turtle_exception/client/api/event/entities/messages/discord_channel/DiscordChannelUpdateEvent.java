package de.turtle_exception.client.api.event.entities.messages.discord_channel;

import de.turtle_exception.client.api.entities.messages.DiscordChannel;
import de.turtle_exception.client.api.event.entities.EntityUpdateEvent;
import org.jetbrains.annotations.NotNull;

public abstract class DiscordChannelUpdateEvent<V> extends DiscordChannelEvent implements EntityUpdateEvent<DiscordChannel, V> {
    private final @NotNull String key;

    protected final V oldValue;
    protected final V newValue;

    public DiscordChannelUpdateEvent(@NotNull DiscordChannel discordChannel, @NotNull String key, V oldValue, V newValue) {
        super(discordChannel);
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
