package de.turtle_exception.client.internal.request.actions.entities.form;

import de.turtle_exception.client.api.entities.form.CompletedForm;
import de.turtle_exception.client.api.entities.form.TemplateForm;
import de.turtle_exception.client.api.request.entities.form.CompletedFormAction;
import de.turtle_exception.client.internal.Provider;
import de.turtle_exception.client.internal.request.actions.EntityAction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public class CompletedFormActionImpl extends EntityAction<CompletedForm> implements CompletedFormAction {
    private long form;
    private long author;
    private long submissionTime;
    private ArrayList<Long> queryResponses = new ArrayList<>();

    public CompletedFormActionImpl(@NotNull Provider provider) {
        super(provider, CompletedForm.class);

        // TODO: checks
    }

    @Override
    protected void updateContent() {
        // TODO
    }

    /* - - - */

    @Override
    public CompletedFormAction setForm(long form) {
        this.form = form;
        return this;
    }

    @Override
    public CompletedFormAction setAuthor(long user) {
        this.author = user;
        return this;
    }

    @Override
    public CompletedFormAction setSubmissionTime(long millis) {
        this.submissionTime = millis;
        return this;
    }

    @Override
    public CompletedFormAction setQueryResponseIds(@NotNull Collection<Long> queryResponses) {
        this.queryResponses = new ArrayList<>(queryResponses);
        return this;
    }

    @Override
    public CompletedFormAction addQueryResponseId(long queryResponse) {
        this.queryResponses.add(queryResponse);
        return this;
    }

    @Override
    public CompletedFormAction removeQueryResponseId(long queryResponse) {
        this.queryResponses.remove(queryResponse);
        return this;
    }
}
