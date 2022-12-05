package de.turtle_exception.client.api.event.entities.form.completed_form;

import de.turtle_exception.client.api.entities.form.CompletedForm;
import de.turtle_exception.client.api.event.entities.EntityEvent;
import org.jetbrains.annotations.NotNull;

public abstract class CompletedFormEvent extends EntityEvent<CompletedForm> {
    public CompletedFormEvent(@NotNull CompletedForm entity) {
        super(entity);
    }
}
