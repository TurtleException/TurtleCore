package de.turtle_exception.client.internal.request.actions.entities.form;

import de.turtle_exception.client.api.entities.form.ContentType;
import de.turtle_exception.client.api.entities.form.QueryElement;
import de.turtle_exception.client.api.request.entities.form.QueryElementAction;
import de.turtle_exception.client.internal.Provider;
import de.turtle_exception.client.internal.request.actions.EntityAction;
import org.jetbrains.annotations.NotNull;

public class QueryElementActionImpl extends EntityAction<QueryElement> implements QueryElementAction {
    private String title;
    private String description;
    private ContentType contentType;

    public QueryElementActionImpl(@NotNull Provider provider) {
        super(provider, QueryElement.class);

        // TODO: checks
    }

    @Override
    protected void updateContent() {
        // TODO
    }

    /* - - - */

    @Override
    public QueryElementAction setTitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    public QueryElementAction setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public QueryElementAction setContentType(ContentType contentType) {
        this.contentType = contentType;
        return this;
    }
}
