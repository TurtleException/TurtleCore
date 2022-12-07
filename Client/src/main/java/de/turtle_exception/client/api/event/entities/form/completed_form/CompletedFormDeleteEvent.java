package de.turtle_exception.client.api.event.entities.form.completed_form;

import de.turtle_exception.client.api.entities.form.CompletedForm;
import de.turtle_exception.client.api.event.entities.EntityDeleteEvent;
import org.jetbrains.annotations.NotNull;

public class CompletedFormDeleteEvent extends CompletedFormEvent implements EntityDeleteEvent<CompletedForm> {
    public CompletedFormDeleteEvent(@NotNull CompletedForm entity) {
        super(entity);
    }
}
