package de.turtle_exception.client.api.event.entities.form.template_form;

import de.turtle_exception.client.api.entities.form.TemplateForm;
import de.turtle_exception.client.api.event.entities.EntityDeleteEvent;
import org.jetbrains.annotations.NotNull;

public class TemplateFormDeleteEvent extends TemplateFormEvent implements EntityDeleteEvent<TemplateForm> {
    public TemplateFormDeleteEvent(@NotNull TemplateForm entity) {
        super(entity);
    }
}
