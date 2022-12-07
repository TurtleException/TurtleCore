package de.turtle_exception.client.api.request.entities.form;

import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.form.TextElement;
import de.turtle_exception.client.api.request.Action;

import java.util.Collection;

/**
 * A TextElementAction is an Action that requests the creation of a new {@link TextElement}, according to the arguments
 * this Action holds. If any required field is missing the server will reject the request and respond with an error.
 * Required fields are all attributes that are not a subclass of {@link Collection}, as these are set to an empty
 * Collection by default.
 * @see TurtleClient#createTextElement()
 */
@SuppressWarnings("unused")
public interface TextElementAction extends Action<TextElement> {
    /**
     * Sets the title of this Element to the provided String.
     * @param title Element title.
     * @return This TextElementAction for chaining convenience.
     */
    TextElementAction setTitle(String title);

    /**
     * Sets the content of this TextElement to the provided String.
     * @param content TextElement content.
     * @return This TextElementAction for chaining convenience.
     */
    TextElementAction setContent(String content);
}
