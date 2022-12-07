package de.turtle_exception.client.api.request.entities.form;

import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.form.QueryElement;
import de.turtle_exception.client.api.entities.form.QueryResponse;
import de.turtle_exception.client.api.request.Action;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * A QueryResponseAction is an Action that requests the creation of a new {@link QueryResponse}, according to the
 * arguments this Action holds. If any required field is missing the server will reject the request and respond with an
 * error. Required fields are all attributes that are not a subclass of {@link Collection}, as these are set to an empty
 * Collection by default.
 * @see TurtleClient#createQueryResponse()
 */
@SuppressWarnings("unused")
public interface QueryResponseAction extends Action<QueryResponse> {
    /**
     * Sets the Query id of this QueryResponse to the provided id.
     * @param query QueryResponse Query id.
     * @return This QueryResponseAction for chaining convenience.
     */
    QueryResponseAction setQueryId(long query);

    /**
     * Sets the Query of this QueryResponse to the provided QueryElement.
     * @param query QueryResponse Query.
     * @return This QueryResponseAction for chaining convenience.
     */
    default QueryResponseAction setQuery(@NotNull QueryElement query) {
        return this.setQueryId(query.getId());
    }

    /**
     * Sets the content of this QueryResponse to the provided String.
     * @param content QueryResponse content.
     * @return This QueryResponseAction for chaining convenience.
     */
    QueryResponseAction setContent(String content);

    /**
     * Sets the content of this QueryResponse to the provided Number.
     * @param content QueryResponse content.
     * @return This QueryResponseAction for chaining convenience.
     */
    default QueryResponseAction setContent(Number content) {
        return this.setContent(content.toString());
    }

    /**
     * Sets the content of this QueryResponse to the provided Boolean.
     * @param content QueryResponse content.
     * @return This QueryResponseAction for chaining convenience.
     */
    default QueryResponseAction setContent(Boolean content) {
        return this.setContent(content.toString());
    }
}
