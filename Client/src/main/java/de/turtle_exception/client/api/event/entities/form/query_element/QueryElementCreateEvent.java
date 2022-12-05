package de.turtle_exception.client.api.event.entities.form.query_element;

import de.turtle_exception.client.api.entities.form.QueryElement;
import de.turtle_exception.client.api.event.entities.EntityCreateEvent;
import org.jetbrains.annotations.NotNull;

public class QueryElementCreateEvent extends QueryElementEvent implements EntityCreateEvent<QueryElement> {
    public QueryElementCreateEvent(@NotNull QueryElement entity) {
        super(entity);
    }
}
