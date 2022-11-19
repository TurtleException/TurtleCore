package de.turtle_exception.client.api.event.entities.user;

import de.turtle_exception.client.api.entities.User;
import org.jetbrains.annotations.NotNull;

public abstract class UserUpdateEvent<T> extends UserEvent {
    private final @NotNull String key;

    protected final T oldValue;
    protected final T newValue;

    public UserUpdateEvent(@NotNull User user, @NotNull String key, T oldValue, T newValue) {
        super(user);
        this.key = key;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public final @NotNull String getKey() {
        return key;
    }

    public T getOldValue() {
        return oldValue;
    }

    public T getNewValue() {
        return newValue;
    }
}
