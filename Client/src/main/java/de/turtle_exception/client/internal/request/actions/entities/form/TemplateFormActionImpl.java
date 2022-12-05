package de.turtle_exception.client.internal.request.actions.entities.form;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.turtle_exception.client.api.entities.form.TemplateForm;
import de.turtle_exception.client.api.request.entities.form.TemplateFormAction;
import de.turtle_exception.client.internal.Provider;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.internal.request.actions.EntityAction;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TemplateFormActionImpl extends EntityAction<TemplateForm> implements TemplateFormAction {
    private String title;
    private ArrayList<Long> queries;

    @SuppressWarnings("CodeBlock2Expr")
    public TemplateFormActionImpl(@NotNull Provider provider) {
        super(provider, TemplateForm.class);

        this.checks.add(json -> { json.get(Keys.Form.TemplateForm.TITLE).getAsString(); });
        this.checks.add(json -> {
            JsonArray arr = json.get(Keys.Form.TemplateForm.QUERIES).getAsJsonArray();
            for (JsonElement entry : arr)
                entry.getAsLong();
        });
    }

    @Override
    protected void updateContent() {
        this.content = new JsonObject();
        this.content.addProperty(Keys.Form.TemplateForm.TITLE, title);

        JsonArray arr = new JsonArray();
        for (Long query : this.queries)
            arr.add(query);
        this.content.add(Keys.Form.TemplateForm.QUERIES, arr);
    }

    /* - - - */

    @Override
    public TemplateFormAction setTitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    public TemplateFormAction setQueryIds(@NonNull List<Long> queries) {
        this.queries = new ArrayList<>(queries);
        return this;
    }

    @Override
    public TemplateFormAction addQueryId(long query) {
        this.queries.add(query);
        return this;
    }

    @Override
    public TemplateFormAction removeQueryId(long query) {
        this.queries.remove(query);
        return this;
    }
}
