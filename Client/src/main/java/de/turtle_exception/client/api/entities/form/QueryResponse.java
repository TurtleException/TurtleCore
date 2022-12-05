package de.turtle_exception.client.api.entities.form;

import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.internal.data.annotations.Key;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.internal.data.annotations.Resource;
import de.turtle_exception.client.internal.data.annotations.Types;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Resource(path = "query_responses", builder = "buildQueryResponse")
@SuppressWarnings("unused")
public interface QueryResponse extends Turtle {
    @Key(name = Keys.Form.QueryResponse.QUERY, sqlType = Types.Form.QueryResponse.QUERY)
    @NotNull QueryElement getQuery();

    @Key(name = Keys.Form.QueryResponse.CONTENT, sqlType = Types.Form.QueryResponse.CONTENT)
    @Nullable String getAsString();

    default boolean isNull() {
        return this.getAsString() == null;
    }

    default @Nullable Boolean getAsBoolean() throws IllegalStateException {
        this.checkType(Boolean.class);

        String content = this.getAsString();
        if (content == null)
            return null;
        return Boolean.parseBoolean(content);
    }

    default @Nullable Double getAsDouble() throws IllegalStateException {
        this.checkType(Double.class);

        String content = this.getAsString();
        if (content == null)
            return null;
        return Double.parseDouble(content);
    }

    default @Nullable Integer getAsInteger() throws IllegalStateException {
        this.checkType(Integer.class);

        String content = this.getAsString();
        if (content == null)
            return null;
        return Integer.parseInt(content);
    }

    private void checkType(@NotNull Class<?> type) throws IllegalStateException {
        if (!this.getQuery().getContentClass().equals(type))
            throw new IllegalStateException("Cannot parse " + type.getSimpleName() + " from " + this.getQuery().getContentType().getTitle());
    }
}
