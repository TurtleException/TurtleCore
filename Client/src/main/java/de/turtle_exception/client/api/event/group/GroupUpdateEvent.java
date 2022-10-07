package de.turtle_exception.client.api.event.group;

import de.turtle_exception.client.api.entities.Group;
import org.jetbrains.annotations.NotNull;

public abstract class GroupUpdateEvent<T> extends GroupEvent {
    protected final T oldValue;
    protected final T newValue;

    public GroupUpdateEvent(@NotNull Group group, T oldValue, T newValue) {
        super(group);
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
