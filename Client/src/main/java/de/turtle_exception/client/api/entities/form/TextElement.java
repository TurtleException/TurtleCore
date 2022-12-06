package de.turtle_exception.client.api.entities.form;

import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.data.annotations.Key;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.internal.data.annotations.Resource;
import de.turtle_exception.client.internal.data.annotations.Types;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** A Form Element that only contains text. */
@Resource(path = "form_element_text", builder = "buildTextElement")
@SuppressWarnings("unused")
public interface TextElement extends Element {
    @Override
    default @NotNull Action<TextElement> update() {
        return this.getClient().retrieveTurtle(this.getId(), TextElement.class);
    }

    /* - TITLE - */

    @Override
    @NotNull Action<TextElement> modifyTitle(@Nullable String title);

    /* - CONTENT - */

    /**
     * Provides the content of this TextElement.
     * @return The TextElement content.
     */
    @Key(name = Keys.Form.TextElement.CONTENT, sqlType = Types.Form.TextElement.CONTENT)
    @NotNull String getContent();

    /**
     * Creates an Action with the instruction to modify this TextElement's content and change it to the provided String.
     * @param content New TextElement content.
     * @return Action that provides the modified {@link TextElement} on completion.
     */
    @NotNull Action<TextElement> modifyContent(@NotNull String content);
}
