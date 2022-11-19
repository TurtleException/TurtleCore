package de.turtle_exception.client.api.event.entities.group;

import de.turtle_exception.client.api.entities.Group;
import org.jetbrains.annotations.NotNull;

public abstract class GroupUpdateEvent<T> extends GroupEvent {
    private final @NotNull String key;

    protected final T oldValue;
    protected final T newValue;

    public GroupUpdateEvent(@NotNull Group group, @NotNull String key, T oldValue, T newValue) {
        super(group);
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
