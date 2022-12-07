package de.turtle_exception.client.internal.request.actions.entities.form;

import com.google.gson.JsonObject;
import de.turtle_exception.client.api.entities.form.QueryResponse;
import de.turtle_exception.client.api.request.entities.form.QueryResponseAction;
import de.turtle_exception.client.internal.Provider;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.internal.request.actions.EntityAction;
import org.jetbrains.annotations.NotNull;

public class QueryResponseActionImpl extends EntityAction<QueryResponse> implements QueryResponseAction {
    private long query;
    private String responseContent;

    @SuppressWarnings("CodeBlock2Expr")
    public QueryResponseActionImpl(@NotNull Provider provider) {
        super(provider, QueryResponse.class);

        this.checks.add(json -> { json.get(Keys.Form.QueryResponse.QUERY).getAsLong(); });
        this.checks.add(json -> { json.get(Keys.Form.QueryResponse.CONTENT).getAsString(); });
    }

    @Override
    protected void updateContent() {
        this.content = new JsonObject();
        this.content.addProperty(Keys.Form.QueryResponse.QUERY, query);
        this.content.addProperty(Keys.Form.QueryResponse.CONTENT, responseContent);
    }

    /* - - - */

    @Override
    public QueryResponseAction setQueryId(long query) {
        this.query = query;
        return this;
    }

    @Override
    public QueryResponseAction setContent(String content) {
        this.responseContent = content;
        return this;
    }
}
