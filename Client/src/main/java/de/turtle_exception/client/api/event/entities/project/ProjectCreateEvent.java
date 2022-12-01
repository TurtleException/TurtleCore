package de.turtle_exception.client.api.event.entities.project;

import de.turtle_exception.client.api.entities.Project;
import de.turtle_exception.client.api.event.entities.EntityCreateEvent;
import org.jetbrains.annotations.NotNull;

public class ProjectCreateEvent extends ProjectEvent implements EntityCreateEvent<Project> {
    public ProjectCreateEvent(@NotNull Project project) {
        super(project);
    }
}
