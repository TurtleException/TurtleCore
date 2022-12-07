package de.turtle_exception.client.api.event.entities.form.text_element;

import de.turtle_exception.client.api.entities.form.TextElement;
import de.turtle_exception.client.api.event.entities.EntityCreateEvent;
import org.jetbrains.annotations.NotNull;

public class TextElementCreateEvent extends TextElementEvent implements EntityCreateEvent<TextElement> {
    public TextElementCreateEvent(@NotNull TextElement entity) {
        super(entity);
    }
}
