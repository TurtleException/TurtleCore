package de.turtle_exception.client.api.event.entities.project;

import de.turtle_exception.client.api.entities.Project;
import de.turtle_exception.client.api.event.entities.EntityEvent;
import org.jetbrains.annotations.NotNull;

public abstract class ProjectEvent extends EntityEvent<Project> {
    public ProjectEvent(@NotNull Project entity) {
        super(entity);
    }

    public @NotNull Project getProject() {
        return this.getEntity();
    }
}
