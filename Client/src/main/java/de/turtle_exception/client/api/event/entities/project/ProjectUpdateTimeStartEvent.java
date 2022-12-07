package de.turtle_exception.client.api.event.entities.project;

import de.turtle_exception.client.api.entities.Project;
import de.turtle_exception.client.internal.data.annotations.Keys;
import org.jetbrains.annotations.NotNull;

public class ProjectUpdateTimeStartEvent extends ProjectUpdateEvent<Long> {
    public ProjectUpdateTimeStartEvent(@NotNull Project project, Long oldValue, Long newValue) {
        super(project, Keys.Project.STATE, oldValue, newValue);
    }
}
