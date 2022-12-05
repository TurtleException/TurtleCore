package de.turtle_exception.client.api.request.entities.form;

import de.turtle_exception.client.api.entities.form.TextElement;
import de.turtle_exception.client.api.request.Action;

@SuppressWarnings("unused")
public interface TextElementAction extends Action<TextElement> {
    TextElementAction setTitle(String title);

    TextElementAction setContent(String content);
}
