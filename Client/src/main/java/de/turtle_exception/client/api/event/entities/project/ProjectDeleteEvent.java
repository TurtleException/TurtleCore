package de.turtle_exception.client.api.event.entities.project;

import de.turtle_exception.client.api.entities.Project;
import de.turtle_exception.client.api.event.entities.EntityDeleteEvent;
import org.jetbrains.annotations.NotNull;

public class ProjectDeleteEvent extends ProjectEvent implements EntityDeleteEvent<Project> {
    public ProjectDeleteEvent(@NotNull Project project) {
        super(project);
    }
}
