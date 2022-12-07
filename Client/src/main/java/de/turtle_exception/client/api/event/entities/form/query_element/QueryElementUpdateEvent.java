package de.turtle_exception.client.api.event.entities.form.query_element;

import de.turtle_exception.client.api.entities.form.QueryElement;
import de.turtle_exception.client.api.event.entities.EntityUpdateEvent;
import org.jetbrains.annotations.NotNull;

public abstract class QueryElementUpdateEvent<V> extends QueryElementEvent implements EntityUpdateEvent<QueryElement, V> {
    private final @NotNull String key;

    protected final V oldValue;
    protected final V newValue;

    public QueryElementUpdateEvent(@NotNull QueryElement entity, @NotNull String key, V oldValue, V newValue) {
        super(entity);
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
