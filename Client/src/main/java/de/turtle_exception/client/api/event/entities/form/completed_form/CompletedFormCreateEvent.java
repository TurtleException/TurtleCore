package de.turtle_exception.client.api.event.entities.form.completed_form;

import de.turtle_exception.client.api.entities.form.CompletedForm;
import de.turtle_exception.client.api.event.entities.EntityCreateEvent;
import org.jetbrains.annotations.NotNull;

public class CompletedFormCreateEvent extends CompletedFormEvent implements EntityCreateEvent<CompletedForm> {
    public CompletedFormCreateEvent(@NotNull CompletedForm entity) {
        super(entity);
    }
}
