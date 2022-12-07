package de.turtle_exception.client.internal.entities.form;

import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.form.Element;
import de.turtle_exception.client.internal.entities.TurtleImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ElementImpl extends TurtleImpl implements Element {
    protected String title;

    protected ElementImpl(@NotNull TurtleClient client, long id, String title) {
        super(client, id);
        this.title = title;
    }

    @Override
    public @Nullable String getTitle() {
        return this.title;
    }
}
