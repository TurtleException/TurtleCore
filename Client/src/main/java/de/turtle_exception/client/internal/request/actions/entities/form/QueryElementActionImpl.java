package de.turtle_exception.client.internal.request.actions.entities.form;

import com.google.gson.JsonObject;
import de.turtle_exception.client.api.entities.form.ContentType;
import de.turtle_exception.client.api.entities.form.QueryElement;
import de.turtle_exception.client.api.request.entities.form.QueryElementAction;
import de.turtle_exception.client.internal.Provider;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.internal.request.actions.EntityAction;
import org.jetbrains.annotations.NotNull;

public class QueryElementActionImpl extends EntityAction<QueryElement> implements QueryElementAction {
    private String title;
    private String description;
    private ContentType contentType;

    @SuppressWarnings("CodeBlock2Expr")
    public QueryElementActionImpl(@NotNull Provider provider) {
        super(provider, QueryElement.class);

        this.checks.add(json -> { json.get(Keys.Form.Element.TITLE).getAsString(); });
        this.checks.add(json -> { json.get(Keys.Form.QueryElement.DESCRIPTION).getAsString(); });
        this.checks.add(json -> { json.get(Keys.Form.QueryElement.CONTENT_TYPE).getAsByte(); });
    }

    @Override
    protected void updateContent() {
        this.content = new JsonObject();
        this.content.addProperty(Keys.Form.Element.TITLE, title);
        this.content.addProperty(Keys.Form.QueryElement.DESCRIPTION, description);
        this.content.addProperty(Keys.Form.QueryElement.CONTENT_TYPE, contentType.getCode());
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
