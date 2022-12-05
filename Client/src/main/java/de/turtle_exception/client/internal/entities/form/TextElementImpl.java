package de.turtle_exception.client.internal.entities.form;

import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.form.TextElement;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.entities.TurtleImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TextElementImpl extends ElementImpl implements TextElement {
    private String content;

    public TextElementImpl(@NotNull TurtleClient client, long id, String title, String content) {
        super(client, id, title);
        this.content = content;
    }

    @Override
    public @NotNull TurtleImpl handleUpdate(@NotNull JsonObject json) {
        // TODO
        return this;
    }

    /* - - - */

    @Override
    public @NotNull Action<TextElement> modifyTitle(@Nullable String title) {
        // TODO
        return null;
    }

    @Override
    public @NotNull String getContent() {
        return this.content;
    }

    @Override
    public @NotNull Action<TextElement> modifyContent(@NotNull String content) {
        // TODO
        return null;
    }
}
