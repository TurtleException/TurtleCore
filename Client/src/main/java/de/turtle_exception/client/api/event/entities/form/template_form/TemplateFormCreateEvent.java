package de.turtle_exception.client.api.event.entities.form.template_form;

import de.turtle_exception.client.api.entities.form.TemplateForm;
import de.turtle_exception.client.api.event.entities.EntityCreateEvent;
import org.jetbrains.annotations.NotNull;

public class TemplateFormCreateEvent extends TemplateFormEvent implements EntityCreateEvent<TemplateForm> {
    public TemplateFormCreateEvent(@NotNull TemplateForm entity) {
        super(entity);
    }
}
