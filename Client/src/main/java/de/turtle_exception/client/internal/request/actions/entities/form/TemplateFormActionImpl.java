package de.turtle_exception.client.internal.request.actions.entities.form;

import de.turtle_exception.client.api.entities.form.TemplateForm;
import de.turtle_exception.client.api.request.entities.form.TemplateFormAction;
import de.turtle_exception.client.internal.Provider;
import de.turtle_exception.client.internal.request.actions.EntityAction;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TemplateFormActionImpl extends EntityAction<TemplateForm> implements TemplateFormAction {
    private String title;
    private ArrayList<Long> queries;

    public TemplateFormActionImpl(@NotNull Provider provider) {
        super(provider, TemplateForm.class);

        // TODO: checks
    }

    @Override
    protected void updateContent() {
        // TODO
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
