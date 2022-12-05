package de.turtle_exception.client.api.event.entities.form.template_form;

import de.turtle_exception.client.api.entities.form.TemplateForm;
import de.turtle_exception.client.api.event.entities.EntityEvent;
import org.jetbrains.annotations.NotNull;

public abstract class TemplateFormEvent extends EntityEvent<TemplateForm> {
    public TemplateFormEvent(@NotNull TemplateForm entity) {
        super(entity);
    }
}
