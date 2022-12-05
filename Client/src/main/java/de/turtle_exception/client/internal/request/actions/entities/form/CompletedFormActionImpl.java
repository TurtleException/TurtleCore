package de.turtle_exception.client.internal.request.actions.entities.form;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.turtle_exception.client.api.entities.form.CompletedForm;
import de.turtle_exception.client.api.entities.form.TemplateForm;
import de.turtle_exception.client.api.request.entities.form.CompletedFormAction;
import de.turtle_exception.client.internal.Provider;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.internal.request.actions.EntityAction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public class CompletedFormActionImpl extends EntityAction<CompletedForm> implements CompletedFormAction {
    private long form;
    private long author;
    private long submissionTime;
    private ArrayList<Long> queryResponses = new ArrayList<>();

    @SuppressWarnings("CodeBlock2Expr")
    public CompletedFormActionImpl(@NotNull Provider provider) {
        super(provider, CompletedForm.class);

        this.checks.add(json -> { json.get(Keys.Form.CompletedForm.FORM).getAsLong(); });
        this.checks.add(json -> { json.get(Keys.Form.CompletedForm.AUTHOR).getAsLong(); });
        this.checks.add(json -> { json.get(Keys.Form.CompletedForm.TIME_SUBMISSION).getAsLong(); });
        this.checks.add(json -> {
            JsonArray arr = json.get(Keys.Form.CompletedForm.QUERY_RESPONSES).getAsJsonArray();
            for (JsonElement entry : arr)
                entry.getAsLong();
        });
    }

    @Override
    protected void updateContent() {
        this.content = new JsonObject();
        this.content.addProperty(Keys.Form.CompletedForm.FORM, form);
        this.content.addProperty(Keys.Form.CompletedForm.AUTHOR, author);
        this.content.addProperty(Keys.Form.CompletedForm.TIME_SUBMISSION, submissionTime);

        JsonArray arr = new JsonArray();
        for (Long queryResponse : this.queryResponses)
            arr.add(queryResponse);
        this.content.add(Keys.Form.CompletedForm.QUERY_RESPONSES, arr);
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
