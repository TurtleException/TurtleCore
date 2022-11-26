package de.turtle_exception.client.api.event.entities.group;

import de.turtle_exception.client.api.entities.Group;
import de.turtle_exception.client.api.event.entities.EntityUpdateEvent;
import org.jetbrains.annotations.NotNull;

public abstract class GroupUpdateEvent<V> extends GroupEvent implements EntityUpdateEvent<Group, V> {
    private final @NotNull String key;

    protected final V oldValue;
    protected final V newValue;

    public GroupUpdateEvent(@NotNull Group group, @NotNull String key, V oldValue, V newValue) {
        super(group);
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
