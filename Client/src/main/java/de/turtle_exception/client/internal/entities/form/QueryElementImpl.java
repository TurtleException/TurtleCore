package de.turtle_exception.client.internal.entities.form;

import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.form.ContentType;
import de.turtle_exception.client.api.entities.form.QueryElement;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.data.annotations.Keys;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class QueryElementImpl extends ElementImpl implements QueryElement {
    private String description;
    private final ContentType contentType;

    public QueryElementImpl(@NotNull TurtleClient client, long id, String title, String description, ContentType contentType) {
        super(client, id, title);
        this.description = description;
        this.contentType = contentType;
    }

    @Override
    public synchronized @NotNull QueryElementImpl handleUpdate(@NotNull JsonObject json) {
        // TODO
        return this;
    }

    /* - - - */

    @Override
    public @NotNull Action<QueryElement> modifyTitle(@Nullable String title) {
        return getClient().getProvider().patch(this, Keys.Form.Element.TITLE, title).andThenParse(QueryElement.class);
    }

    @Override
    public @Nullable String getDescription() {
        return this.description;
    }

    @Override
    public @NotNull Action<QueryElement> modifyDescription(@Nullable String description) {
        return getClient().getProvider().patch(this, Keys.Form.QueryElement.DESCRIPTION, description).andThenParse(QueryElement.class);
    }

    @Override
    public @NotNull ContentType getContentType() {
        return this.contentType;
    }
}
