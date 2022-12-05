package de.turtle_exception.client.internal.request.actions.entities.form;

import de.turtle_exception.client.api.entities.form.QueryResponse;
import de.turtle_exception.client.api.request.entities.form.QueryResponseAction;
import de.turtle_exception.client.internal.Provider;
import de.turtle_exception.client.internal.request.actions.EntityAction;
import org.jetbrains.annotations.NotNull;

public class QueryResponseActionImpl extends EntityAction<QueryResponse> implements QueryResponseAction {
    private long query;
    private String content;

    public QueryResponseActionImpl(@NotNull Provider provider) {
        super(provider, QueryResponse.class);

        // TODO: checks
    }

    @Override
    protected void updateContent() {
        // TODO
    }

    /* - - - */

    @Override
    public QueryResponseAction setQueryId(long query) {
        this.query = query;
        return this;
    }

    @Override
    public QueryResponseAction setContent(String content) {
        this.content = content;
        return this;
    }
}
