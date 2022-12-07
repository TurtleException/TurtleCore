package de.turtle_exception.client.api.event.entities.project;

import de.turtle_exception.client.api.entities.Project;
import de.turtle_exception.client.internal.data.annotations.Keys;
import org.jetbrains.annotations.NotNull;

public class ProjectUpdateTimeEndEvent extends ProjectUpdateEvent<Long> {
    public ProjectUpdateTimeEndEvent(@NotNull Project project, Long oldValue, Long newValue) {
        super(project, Keys.Project.STATE, oldValue, newValue);
    }
}
