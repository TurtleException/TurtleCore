package de.turtle_exception.client.api.event.entities.form.text_element;

import de.turtle_exception.client.api.entities.form.TextElement;
import de.turtle_exception.client.api.event.entities.EntityEvent;
import org.jetbrains.annotations.NotNull;

public abstract class TextElementEvent extends EntityEvent<TextElement> {
    public TextElementEvent(@NotNull TextElement entity) {
        super(entity);
    }
}
