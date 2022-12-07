package de.turtle_exception.client.api.event.entities.form.query_element;

import de.turtle_exception.client.api.entities.form.QueryElement;
import de.turtle_exception.client.internal.data.annotations.Keys;
import org.jetbrains.annotations.NotNull;

public class QueryElementUpdateRequiredEvent extends QueryElementUpdateEvent<Boolean> {
    public QueryElementUpdateRequiredEvent(@NotNull QueryElement entity, Boolean oldValue, Boolean newValue) {
        super(entity, Keys.Form.QueryElement.REQUIRED, oldValue, newValue);
    }
}
