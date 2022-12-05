package de.turtle_exception.client.internal.request.actions.entities.form;

import de.turtle_exception.client.api.entities.form.TextElement;
import de.turtle_exception.client.api.request.entities.form.TextElementAction;
import de.turtle_exception.client.internal.Provider;
import de.turtle_exception.client.internal.request.actions.EntityAction;
import org.jetbrains.annotations.NotNull;

public class TextElementActionImpl extends EntityAction<TextElement> implements TextElementAction {
    private String title;
    private String content;

    public TextElementActionImpl(@NotNull Provider provider) {
        super(provider, TextElement.class);

        // TODO: checks
    }

    @Override
    protected void updateContent() {
        // TODO
    }

    /* - - - */

    @Override
    public TextElementAction setTitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    public TextElementAction setContent(String content) {
        this.content = content;
        return this;
    }
}
