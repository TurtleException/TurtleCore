package de.turtle_exception.client.api.event.entities.project;

import de.turtle_exception.client.api.entities.Project;
import de.turtle_exception.client.api.entities.attributes.ProjectState;
import de.turtle_exception.client.internal.data.annotations.Keys;
import org.jetbrains.annotations.NotNull;

public class ProjectUpdateStateEvent extends ProjectUpdateEvent<ProjectState> {
    public ProjectUpdateStateEvent(@NotNull Project project, ProjectState oldValue, ProjectState newValue) {
        super(project, Keys.Project.STATE, oldValue, newValue);
    }
}
