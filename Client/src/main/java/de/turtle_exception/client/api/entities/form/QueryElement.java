package de.turtle_exception.client.api.entities.form;

import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.data.annotations.Key;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.internal.data.annotations.Resource;
import de.turtle_exception.client.internal.data.annotations.Types;
import de.turtle_exception.fancyformat.FormatText;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** A Form Element that queries the User for input of a specific {@link ContentType}. */
@Resource(path = "queries", builder = "buildQueryElement")
@SuppressWarnings("unused")
public interface QueryElement extends Element {
    @Override
    default @NotNull Action<QueryElement> update() {
        return this.getClient().retrieveTurtle(this.getId(), QueryElement.class);
    }

    /* - TITLE - */

    @Override
    @NotNull Action<QueryElement> modifyTitle(@Nullable String title);

    /* - DESCRIPTION - */

    /**
     * Provides the description of this QueryElement.
     * <p> Descriptions also may be {@code null}, if they have not been set.
     * @return The Query description (possibly {@code null}).
     */
    @Key(name = Keys.Form.QueryElement.DESCRIPTION, sqlType = Types.Form.QueryElement.DESCRIPTION)
    @Nullable FormatText getDescription();

    /**
     * Creates an Action with the instruction to modify this QueryElement's description and change it to the provided FormatText.
     * @param description New Query description.
     * @return Action that provides the modified {@link QueryElement} on completion.
     */
    @NotNull Action<QueryElement> modifyDescription(@Nullable FormatText description);

    /* - CONTENT TYPE - */

    /**
     * Provides the ContentType of this Query. See {@link ContentType} documentation for more information.
     * @return The Query ContentType.
     */
    @NotNull ContentType getContentType();

    /**
     * Provides the ContentType of this Query in its {@code byte} representation. This method exists mainly for
     * serialization purposes, as it is a rather inefficient shortcut for {@code QueryElement.getContentType().getType()}.
     * @return The ContentType of this Query as a {@code byte}.
     */
    @Key(name = Keys.Form.QueryElement.CONTENT_TYPE, sqlType = Types.Form.QueryElement.CONTENT_TYPE)
    default byte getContentTypeCode() {
        return this.getContentType().getCode();
    }

    /**
     * Provides the Java representation of this Query's {@link ContentType}.
     * <p> This is a shortcut for {@code QueryElement.getContentType().getType()}.
     * @return Java type class.
     */
    default @NotNull Class<?> getContentClass() {
        return this.getContentType().getType();
    }

    /* - REQUIRED - */

    /**
     * Provides the 'required' flag of this QueryElement.
     * @return Value of the 'required' flag.
     */
    @Key(name = Keys.Form.QueryElement.REQUIRED, sqlType = Types.Form.QueryElement.REQUIRED)
    boolean getRequired();

    /**
     * Creates an Action with the instruction to modify this QueryElement's 'required' flag and change it to the
     * provided boolean.
     * @param b New value for the 'required' flag.
     * @return Action that provides the modified {@link QueryElement} on completion.
     */
    @NotNull Action<QueryElement> modifyRequired(boolean b);
}
