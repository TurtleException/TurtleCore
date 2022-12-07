package de.turtle_exception.client.api.entities.form;

import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.data.annotations.Key;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.internal.data.annotations.Resource;
import de.turtle_exception.client.internal.data.annotations.Types;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** A {@link QueryElement} that has been submitted by a {@link User}. */
@Resource(path = "query_responses", builder = "buildQueryResponse")
@SuppressWarnings("unused")
public interface QueryResponse extends Turtle {
    @Override
    default @NotNull Action<QueryResponse> update() {
        return this.getClient().retrieveTurtle(this.getId(), QueryResponse.class);
    }

    /* - QUERY - */

    /**
     * Provides the initial {@link QueryElement} of this QueryResponse. A QueryElement may have multiple QueryResponses.
     * @return The initial QueryElement.
     */
    @Key(name = Keys.Form.QueryResponse.QUERY, sqlType = Types.Form.QueryResponse.QUERY)
    @NotNull QueryElement getQuery();

    /* - CONTENT - */

    /**
     * Provides the content of this QueryResponse as a {@link String}.
     * <p> This method provides the raw representation of the content in the backing database. Any other types are
     * handled by calling this method and parsing them to the appropriate data type.
     * @return QueryResponse String content (may be {@code null}).
     * @see QueryResponse#getAsBoolean()
     * @see QueryResponse#getAsDouble()
     * @see QueryResponse#getAsInteger()
     */
    @Key(name = Keys.Form.QueryResponse.CONTENT, sqlType = Types.Form.QueryResponse.CONTENT)
    @Nullable String getAsString();

    /**
     * Returns {@code true} if the content of this QueryResponse is {@code null}.
     * @return true, if {@code QueryResponse.getAsString()} is {@code null}.
     */
    default boolean isNull() {
        return this.getAsString() == null;
    }

    /**
     * Provides the content of this QueryResponse as a {@link Boolean}.
     * <p> This method will complete exceptional if the underlying QueryElement has an incompatible {@link ContentType}.
     * @return QueryResponse Boolean content (may be {@code null}).
     * @throws IllegalStateException if the raw content of this QueryResponse could not be parsed to a Boolean.
     */
    default @Nullable Boolean getAsBoolean() throws IllegalStateException {
        this.checkType(Boolean.class);

        String content = this.getAsString();
        if (content == null)
            return null;
        return Boolean.parseBoolean(content);
    }

    /**
     * Provides the content of this QueryResponse as a {@link Double}.
     * <p> This method will complete exceptional if the underlying QueryElement has an incompatible {@link ContentType}.
     * @return QueryResponse Double content (may be {@code null}).
     * @throws IllegalStateException if the raw content of this QueryResponse could not be parsed to a Double.
     */
    default @Nullable Double getAsDouble() throws IllegalStateException {
        this.checkType(Double.class);

        String content = this.getAsString();
        if (content == null)
            return null;
        return Double.parseDouble(content);
    }

    /**
     * Provides the content of this QueryResponse as a {@link Integer}.
     * <p> This method will complete exceptional if the underlying QueryElement has an incompatible {@link ContentType}.
     * @return QueryResponse Integer content (may be {@code null}).
     * @throws IllegalStateException if the raw content of this QueryResponse could not be parsed to a Integer.
     */
    default @Nullable Integer getAsInteger() throws IllegalStateException {
        this.checkType(Integer.class);

        String content = this.getAsString();
        if (content == null)
            return null;
        return Integer.parseInt(content);
    }

    /**
     * Convenience method to check whether the provided Class is compatible as type for the {@link ContentType} of the
     * underlying {@link QueryElement}. This method will complete silently if the type is compatible, otherwise an
     * {@link IllegalStateException} will be thrown.
     * @param type Some Class
     * @throws IllegalStateException if {@code type} is not compatible with the ContentType.
     */
    private void checkType(@NotNull Class<?> type) throws IllegalStateException {
        if (!this.getQuery().getContentClass().equals(type))
            throw new IllegalStateException("Cannot parse " + type.getSimpleName() + " from " + this.getQuery().getContentType().getTitle());
    }
}
