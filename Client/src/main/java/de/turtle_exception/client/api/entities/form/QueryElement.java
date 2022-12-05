package de.turtle_exception.client.api.entities.form;

import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.data.annotations.Key;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.internal.data.annotations.Resource;
import de.turtle_exception.client.internal.data.annotations.Types;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Resource(path = "queries", builder = "buildQuery")
@SuppressWarnings("unused")
public interface QueryElement extends Element {
    @Override
    @NotNull Action<QueryElement> modifyTitle(@Nullable String title);

    @Key(name = Keys.Form.QueryElement.DESCRIPTION, sqlType = Types.Form.QueryElement.DESCRIPTION)
    @Nullable String getDescription();

    @NotNull Action<QueryElement> modifyDescription(@Nullable String description);

    @NotNull ContentType getContentType();

    @Key(name = Keys.Form.QueryElement.CONTENT_TYPE, sqlType = Types.Form.QueryElement.CONTENT_TYPE)
    default byte getContentTypeCode() {
        return this.getContentType().getCode();
    }

    default @NotNull Class<?> getContentClass() {
        return this.getContentType().getType();
    }
}
