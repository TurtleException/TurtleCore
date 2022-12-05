package de.turtle_exception.client.api.event.entities.form.query_response;

import de.turtle_exception.client.api.entities.form.QueryResponse;
import de.turtle_exception.client.api.event.entities.EntityCreateEvent;
import org.jetbrains.annotations.NotNull;

public class QueryResponseCreateEvent extends QueryResponseEvent implements EntityCreateEvent<QueryResponse> {
    public QueryResponseCreateEvent(@NotNull QueryResponse entity) {
        super(entity);
    }
}
