package de.turtle_exception.client.internal.entities.form;

import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.form.TextElement;
import de.turtle_exception.client.api.event.entities.form.text_element.TextElementUpdateContentEvent;
import de.turtle_exception.client.api.event.entities.form.text_element.TextElementUpdateTitleEvent;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.fancyformat.Format;
import de.turtle_exception.fancyformat.FormatText;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TextElementImpl extends ElementImpl implements TextElement {
    private FormatText content;

    public TextElementImpl(@NotNull TurtleClient client, long id, String title, FormatText content) {
        super(client, id, title);
        this.content = content;
    }

    @Override
    public synchronized @NotNull TextElementImpl handleUpdate(@NotNull JsonObject json) {
        this.apply(json, Keys.Form.Element.TITLE, element -> {
            String old = this.title;
            this.title = element.getAsString();
            this.fireEvent(new TextElementUpdateTitleEvent(this, old, this.title));
        });
        this.apply(json, Keys.Form.TextElement.CONTENT, element -> {
            FormatText old = this.content;
            this.content = getClient().getFormatter().newText(element.getAsString(), Format.TURTLE);
            this.fireEvent(new TextElementUpdateContentEvent(this, old, this.content));
        });
        return this;
    }

    /* - - - */

    @Override
    public @NotNull Action<TextElement> modifyTitle(@Nullable String title) {
        return getClient().getProvider().patch(this, Keys.Form.Element.TITLE, title).andThenParse(TextElement.class);
    }

    @Override
    public @NotNull FormatText getContent() {
        return this.content;
    }

    @Override
    public @NotNull Action<TextElement> modifyContent(@NotNull FormatText content) {
        return getClient().getProvider().patch(this, Keys.Form.TextElement.CONTENT, content).andThenParse(TextElement.class);
    }
}
