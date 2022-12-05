package de.turtle_exception.client.api.event.entities.form.query_element;

import de.turtle_exception.client.api.entities.form.QueryElement;
import de.turtle_exception.client.api.event.entities.EntityEvent;
import org.jetbrains.annotations.NotNull;

public abstract class QueryElementEvent extends EntityEvent<QueryElement> {
    public QueryElementEvent(@NotNull QueryElement entity) {
        super(entity);
    }
}
