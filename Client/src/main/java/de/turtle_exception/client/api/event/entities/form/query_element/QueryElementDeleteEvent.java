package de.turtle_exception.client.api.event.entities.form.query_element;

import de.turtle_exception.client.api.entities.form.QueryElement;
import de.turtle_exception.client.api.event.entities.EntityDeleteEvent;
import org.jetbrains.annotations.NotNull;

public class QueryElementDeleteEvent extends QueryElementEvent implements EntityDeleteEvent<QueryElement> {
    public QueryElementDeleteEvent(@NotNull QueryElement entity) {
        super(entity);
    }
}
