package de.turtle_exception.client.api.event.entities.form.query_element;

import de.turtle_exception.client.api.entities.form.QueryElement;
import de.turtle_exception.client.internal.data.annotations.Keys;
import org.jetbrains.annotations.NotNull;

public class QueryElementUpdateDescriptionEvent extends QueryElementUpdateEvent<String> {
    public QueryElementUpdateDescriptionEvent(@NotNull QueryElement entity, String oldValue, String newValue) {
        super(entity, Keys.Form.QueryElement.DESCRIPTION, oldValue, newValue);
    }
}
