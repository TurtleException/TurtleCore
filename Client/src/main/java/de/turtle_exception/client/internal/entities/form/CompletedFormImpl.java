package de.turtle_exception.client.internal.entities.form;

import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.entities.form.CompletedForm;
import de.turtle_exception.client.api.entities.form.QueryResponse;
import de.turtle_exception.client.api.entities.form.TemplateForm;
import de.turtle_exception.client.internal.entities.TurtleImpl;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CompletedFormImpl extends TurtleImpl implements CompletedForm {
    private final TemplateForm form;
    private final User author;
    private final long submissionTime;
    private final List<QueryResponse> queryResponses;

    public CompletedFormImpl(@NotNull TurtleClient client, long id, TemplateForm form, User author, long submissionTime, List<QueryResponse> queryResponses) {
        super(client, id);
        this.form = form;
        this.author = author;
        this.submissionTime = submissionTime;
        this.queryResponses = queryResponses;
    }

    @Override
    public synchronized @NotNull CompletedFormImpl handleUpdate(@NotNull JsonObject json) {
        // this resource is immutable
        return this;
    }

    /* - - - */

    @Override
    public @NotNull TemplateForm getForm() {
        return this.form;
    }

    @Override
    public @NotNull User getAuthor() {
        return this.author;
    }

    @Override
    public long getSubmissionTimeMillis() {
        return this.submissionTime;
    }

    @Override
    public @NotNull List<QueryResponse> getQueryResponses() {
        return List.copyOf(this.queryResponses);
    }
}
