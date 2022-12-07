package de.turtle_exception.client.api.event.entities.project;

import de.turtle_exception.client.api.entities.Project;
import de.turtle_exception.client.api.entities.attributes.ProjectState;
import de.turtle_exception.client.api.entities.form.CompletedForm;
import de.turtle_exception.client.api.entities.form.TemplateForm;
import de.turtle_exception.client.internal.data.annotations.Keys;
import org.jetbrains.annotations.NotNull;

public class ProjectUpdateApplicationFormEvent extends ProjectUpdateEvent<Long> {
    public ProjectUpdateApplicationFormEvent(@NotNull Project project, Long oldValue, Long newValue) {
        super(project, Keys.Project.STATE, oldValue, newValue);
    }

    public TemplateForm getOldForm() {
        return this.getClient().getTurtleById(this.getOldValue(), TemplateForm.class);
    }

    public TemplateForm getNewForm() {
        return this.getClient().getTurtleById(this.getNewValue(), TemplateForm.class);
    }
}
