package de.turtle_exception.client.api.event.entities.project;

import de.turtle_exception.client.api.entities.Project;
import de.turtle_exception.client.api.entities.attributes.ProjectState;
import de.turtle_exception.client.api.entities.form.CompletedForm;
import de.turtle_exception.client.api.entities.form.TemplateForm;
import de.turtle_exception.client.internal.data.annotations.Keys;
import org.jetbrains.annotations.NotNull;

public class ProjectUpdateApplicationFormEvent extends ProjectUpdateEvent<TemplateForm> {
    public ProjectUpdateApplicationFormEvent(@NotNull Project project, TemplateForm oldValue, TemplateForm newValue) {
        super(project, Keys.Project.STATE, oldValue, newValue);
    }
}
