package de.turtle_exception.client.api.event.entities.form.query_element;

import de.turtle_exception.client.api.entities.form.QueryElement;
import de.turtle_exception.client.internal.data.annotations.Keys;
import org.jetbrains.annotations.NotNull;

public class QueryElementUpdateTitleEvent extends QueryElementUpdateEvent<String> {
    public QueryElementUpdateTitleEvent(@NotNull QueryElement entity, String oldValue, String newValue) {
        super(entity, Keys.Form.Element.TITLE, oldValue, newValue);
    }
}
