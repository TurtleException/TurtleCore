package de.turtle_exception.client.api.event.entities.form.query_response;

import de.turtle_exception.client.api.entities.form.QueryResponse;
import de.turtle_exception.client.api.event.entities.EntityDeleteEvent;
import org.jetbrains.annotations.NotNull;

public class QueryResponseDeleteEvent extends QueryResponseEvent implements EntityDeleteEvent<QueryResponse> {
    public QueryResponseDeleteEvent(@NotNull QueryResponse entity) {
        super(entity);
    }
}
