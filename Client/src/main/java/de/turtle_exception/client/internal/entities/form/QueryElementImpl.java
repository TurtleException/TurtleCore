package de.turtle_exception.client.internal.entities.form;

import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.form.ContentType;
import de.turtle_exception.client.api.entities.form.QueryElement;
import de.turtle_exception.client.api.event.entities.form.query_element.QueryElementUpdateDescriptionEvent;
import de.turtle_exception.client.api.event.entities.form.query_element.QueryElementUpdateRequiredEvent;
import de.turtle_exception.client.api.event.entities.form.query_element.QueryElementUpdateTitleEvent;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.data.annotations.Keys;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class QueryElementImpl extends ElementImpl implements QueryElement {
    private String description;
    private final ContentType contentType;
    private boolean required;

    public QueryElementImpl(@NotNull TurtleClient client, long id, String title, String description, ContentType contentType, boolean required) {
        super(client, id, title);
        this.description = description;
        this.contentType = contentType;
        this.required = required;
    }

    @Override
    public synchronized @NotNull QueryElementImpl handleUpdate(@NotNull JsonObject json) {
        this.apply(json, Keys.Form.Element.TITLE, element -> {
            String old = this.title;
            this.title = element.getAsString();
            this.fireEvent(new QueryElementUpdateTitleEvent(this, old, this.title));
        });
        this.apply(json, Keys.Form.QueryElement.DESCRIPTION, element -> {
            String old = this.description;
            this.description = element.getAsString();
            this.fireEvent(new QueryElementUpdateDescriptionEvent(this, old, this.description));
        });
        this.apply(json, Keys.Form.QueryElement.REQUIRED, element -> {
            boolean old = this.required;
            this.required = element.getAsBoolean();
            this.fireEvent(new QueryElementUpdateRequiredEvent(this, old, this.required));
        });
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

    @Override
    public boolean getRequired() {
        return this.required;
    }

    @Override
    public @NotNull Action<QueryElement> modifyRequired(boolean b) {
        return getClient().getProvider().patch(this, Keys.Form.QueryElement.REQUIRED, b).andThenParse(QueryElement.class);
    }
}
