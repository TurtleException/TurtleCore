package de.turtle_exception.client.api.event.entities.form.text_element;

import de.turtle_exception.client.api.entities.form.TextElement;
import de.turtle_exception.client.api.event.entities.EntityDeleteEvent;
import org.jetbrains.annotations.NotNull;

public class TextElementDeleteEvent extends TextElementEvent implements EntityDeleteEvent<TextElement> {
    public TextElementDeleteEvent(@NotNull TextElement entity) {
        super(entity);
    }
}
