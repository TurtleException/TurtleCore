package de.turtle_exception.client.internal.entities.form;

import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.form.QueryElement;
import de.turtle_exception.client.api.entities.form.TemplateForm;
import de.turtle_exception.client.internal.entities.TurtleImpl;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TemplateFormImpl extends TurtleImpl implements TemplateForm {
    private final String title;
    private final List<QueryElement> queries;

    public TemplateFormImpl(@NotNull TurtleClient client, long id, String title, List<QueryElement> queries) {
        super(client, id);
        this.title = title;
        this.queries = queries;
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
    public @NotNull List<QueryElement> getQueries() {
        return List.copyOf(this.queries);
    }
}
