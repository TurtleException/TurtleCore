package de.turtle_exception.client.api.request.entities.form;

import de.turtle_exception.client.api.entities.form.QueryElement;
import de.turtle_exception.client.api.entities.form.QueryResponse;
import de.turtle_exception.client.api.request.Action;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public interface QueryResponseAction extends Action<QueryResponse> {
    QueryResponseAction setQueryId(long query);

    default QueryResponseAction setQuery(@NotNull QueryElement query) {
        return this.setQueryId(query.getId());
    }

    QueryResponseAction setContent(String content);

    default QueryResponseAction setContent(Number content) {
        return this.setContent(content.toString());
    }

    default QueryResponseAction setContent(Boolean content) {
        return this.setContent(content.toString());
    }
}
