package de.turtle_exception.client.api.event.user;

import de.turtle_exception.client.api.entities.User;
import org.jetbrains.annotations.NotNull;

public abstract class UserUpdateEvent<T> extends UserEvent {
    protected final T oldValue;
    protected final T newValue;

    public UserUpdateEvent(@NotNull User user, T oldValue, T newValue) {
        super(user);
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public T getOldValue() {
        return oldValue;
    }

    public T getNewValue() {
        return newValue;
    }
}
