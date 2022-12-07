package de.turtle_exception.client.internal.entities.form;

import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.form.Element;
import de.turtle_exception.client.api.entities.form.QueryElement;
import de.turtle_exception.client.api.entities.form.TemplateForm;
import de.turtle_exception.client.internal.entities.TurtleImpl;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TemplateFormImpl extends TurtleImpl implements TemplateForm {
    private final String title;
    private final List<Long> elements;

    public TemplateFormImpl(@NotNull TurtleClient client, long id, String title, List<Long> elements) {
        super(client, id);
        this.title = title;
        this.elements = elements;
    }

    @Override
    public synchronized @NotNull TemplateFormImpl handleUpdate(@NotNull JsonObject json) {
        // this resource is immutable
        return this;
    }

    /* - - - */

    @Override
    public @NotNull String getTitle() {
        return this.title;
    }

    @Override
    public @NotNull List<Long> getElementIds() {
        return List.copyOf(this.elements);
    }
}
