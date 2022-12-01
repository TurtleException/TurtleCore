package de.turtle_exception.client.api.event.entities.project;

import de.turtle_exception.client.api.entities.Project;
import de.turtle_exception.client.internal.data.annotations.Keys;
import org.jetbrains.annotations.NotNull;

public class ProjectUpdateTitleEvent extends ProjectUpdateEvent<String> {
    public ProjectUpdateTitleEvent(@NotNull Project project, String oldValue, String newValue) {
        super(project, Keys.Project.TITLE, oldValue, newValue);
    }
}
