package de.turtle_exception.client.internal.request.actions.entities.form;

import com.google.gson.JsonObject;
import de.turtle_exception.client.api.entities.form.TextElement;
import de.turtle_exception.client.api.request.entities.form.TextElementAction;
import de.turtle_exception.client.internal.Provider;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.internal.request.actions.EntityAction;
import de.turtle_exception.fancyformat.Format;
import de.turtle_exception.fancyformat.FormatText;
import org.jetbrains.annotations.NotNull;

public class TextElementActionImpl extends EntityAction<TextElement> implements TextElementAction {
    private String title;
    private FormatText textContent;

    @SuppressWarnings("CodeBlock2Expr")
    public TextElementActionImpl(@NotNull Provider provider) {
        super(provider, TextElement.class);

        this.checks.add(json -> { json.get(Keys.Form.Element.TITLE).getAsString(); });
        this.checks.add(json -> { json.get(Keys.Form.TextElement.CONTENT).getAsString(); });
    }

    @Override
    protected void updateContent() {
        this.content = new JsonObject();
        this.content.addProperty(Keys.Form.Element.TITLE, title);
        this.content.addProperty(Keys.Form.TextElement.CONTENT, textContent.toString(Format.TURTLE));
    }

    /* - - - */

    @Override
    public TextElementAction setTitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    public TextElementAction setContent(FormatText content) {
        this.textContent = content;
        return this;
    }
}
