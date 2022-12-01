package de.turtle_exception.client.api.event.entities.project;

import de.turtle_exception.client.api.entities.Project;
import de.turtle_exception.client.api.event.entities.EntityUpdateEvent;
import org.jetbrains.annotations.NotNull;

public abstract class ProjectUpdateEvent<V> extends ProjectEvent implements EntityUpdateEvent<Project, V> {
    private final @NotNull String key;

    protected final V oldValue;
    protected final V newValue;

    public ProjectUpdateEvent(@NotNull Project project, @NotNull String key, V oldValue, V newValue) {
        super(project);
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
