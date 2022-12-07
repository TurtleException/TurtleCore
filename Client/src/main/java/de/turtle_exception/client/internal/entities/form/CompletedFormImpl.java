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
    private final long form;
    private final long author;
    private final long submissionTime;
    private final List<Long> queryResponses;

    public CompletedFormImpl(@NotNull TurtleClient client, long id, long form, long author, long submissionTime, List<Long> queryResponses) {
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
    public long getFormId() {
        return this.form;
    }

    @Override
    public long getAuthorId() {
        return this.author;
    }

    @Override
    public long getSubmissionTimeMillis() {
        return this.submissionTime;
    }

    @Override
    public @NotNull List<Long> getQueryResponseIds() {
        return List.copyOf(this.queryResponses);
    }
}
