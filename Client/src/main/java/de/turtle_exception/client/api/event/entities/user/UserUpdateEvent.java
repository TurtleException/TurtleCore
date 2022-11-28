package de.turtle_exception.client.api.event.entities.user;

import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.event.entities.EntityUpdateEvent;
import org.jetbrains.annotations.NotNull;

public abstract class UserUpdateEvent<V> extends UserEvent implements EntityUpdateEvent<User, V> {
    private final @NotNull String key;

    protected final V oldValue;
    protected final V newValue;

    public UserUpdateEvent(@NotNull User user, @NotNull String key, V oldValue, V newValue) {
        super(user);
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
