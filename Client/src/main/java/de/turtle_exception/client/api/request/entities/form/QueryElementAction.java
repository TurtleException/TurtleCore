package de.turtle_exception.client.api.request.entities.form;

import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.form.ContentType;
import de.turtle_exception.client.api.entities.form.QueryElement;
import de.turtle_exception.client.api.request.Action;

import java.util.Collection;

/**
 * A QueryElementAction is an Action that requests the creation of a new {@link QueryElement}, according to the
 * arguments this Action holds. If any required field is missing the server will reject the request and respond with an
 * error. Required fields are all attributes that are not a subclass of {@link Collection}, as these are set to an empty
 * Collection by default.
 * @see TurtleClient#createQueryElement()
 */
@SuppressWarnings("unused")
public interface QueryElementAction extends Action<QueryElement> {
    /**
     * Sets the title of this QueryElement to the provided String.
     * @param title Query title.
     * @return This QueryElementAction for chaining convenience.
     */
    QueryElementAction setTitle(String title);

    /**
     * Sets the description of this QueryElement to the provided String.
     * @param description Query description.
     * @return This QueryElementAction for chaining convenience.
     */
    QueryElementAction setDescription(String description);

    /**
     * Sets the content type of this QueryElement to the provided ContentType.
     * @param contentType Query content type.
     * @return This QueryElementAction for chaining convenience.
     */
    QueryElementAction setContentType(ContentType contentType);

    /**
     * Sets the 'required' flag of this QueryElement to the provided boolean.
     * @param required Query 'required' flag.
     * @return This QueryElementAction for chaining convenience.
     */
    QueryElementAction setRequired(boolean required);
}
