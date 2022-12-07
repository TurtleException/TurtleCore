package de.turtle_exception.client.api.event.entities.form.query_response;

import de.turtle_exception.client.api.entities.form.QueryResponse;
import de.turtle_exception.client.api.event.entities.EntityEvent;
import org.jetbrains.annotations.NotNull;

public abstract class QueryResponseEvent extends EntityEvent<QueryResponse> {
    public QueryResponseEvent(@NotNull QueryResponse entity) {
        super(entity);
    }
}
