package de.turtle_exception.client.api.event.entities.form.text_element;

import de.turtle_exception.client.api.entities.form.TextElement;
import de.turtle_exception.client.internal.data.annotations.Keys;
import org.jetbrains.annotations.NotNull;

public class TextElementUpdateContentEvent extends TextElementUpdateEvent<String> {
    public TextElementUpdateContentEvent(@NotNull TextElement entity, String oldValue, String newValue) {
        super(entity, Keys.Form.TextElement.CONTENT, oldValue, newValue);
    }
}
