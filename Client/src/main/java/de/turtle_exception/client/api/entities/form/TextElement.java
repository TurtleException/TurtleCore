package de.turtle_exception.client.api.entities.form;

import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.data.annotations.Key;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.internal.data.annotations.Resource;
import de.turtle_exception.client.internal.data.annotations.Types;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Resource(path = "form_element_text", builder = "buildTextElement")
@SuppressWarnings("unused")
public interface TextElement extends Element {
    @Override
    @NotNull Action<TextElement> modifyTitle(@Nullable String title);

    @Key(name = Keys.Form.TextElement.CONTENT, sqlType = Types.Form.TextElement.CONTENT)
    @NotNull String getContent();

    @NotNull Action<TextElement> modifyContent(@NotNull String content);
}
