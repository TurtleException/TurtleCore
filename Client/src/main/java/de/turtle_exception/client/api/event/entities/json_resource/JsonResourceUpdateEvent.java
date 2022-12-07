package de.turtle_exception.client.api.event.entities.json_resource;

import de.turtle_exception.client.api.entities.JsonResource;
import de.turtle_exception.client.api.event.entities.EntityUpdateEvent;
import org.jetbrains.annotations.NotNull;

public class JsonResourceUpdateEvent<V> extends JsonResourceEvent implements EntityUpdateEvent<JsonResource, V> {
    private final @NotNull String key;

    protected final V oldValue;
    protected final V newValue;

    public JsonResourceUpdateEvent(@NotNull JsonResource jsonResource, @NotNull String key, V oldValue, V newValue) {
        super(jsonResource);
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
