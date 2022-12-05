package de.turtle_exception.client.api.entities.form;

import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.internal.data.annotations.*;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.List;

@Resource(path = "forms_completed", builder = "buildCompletedForm")
@SuppressWarnings("unused")
public interface CompletedForm extends Turtle {
    @Key(name = Keys.Form.CompletedForm.FORM, relation = Relation.MANY_TO_ONE, sqlType = Types.Form.CompletedForm.FORM)
    @NotNull TemplateForm getForm();

    @Key(name = Keys.Form.CompletedForm.AUTHOR, sqlType = Types.Form.CompletedForm.AUTHOR)
    @NotNull User getAuthor();

    @Key(name = Keys.Form.CompletedForm.TIME_SUBMISSION, sqlType = Types.Form.CompletedForm.TIME_SUBMISSION)
    long getSubmissionTimeMillis();

    default @NotNull Instant getSubmissionTime() {
        return Instant.ofEpochMilli(this.getSubmissionTimeMillis());
    }

    @Key(name = Keys.Form.CompletedForm.QUERY_RESPONSES, relation = Relation.ONE_TO_MANY, sqlType = Types.Form.CompletedForm.QUERY_RESPONSES)
    @Relational(table = "form_query_responses", self = "form", foreign = "query_response", type = QueryResponse.class)
    @NotNull List<QueryResponse> getQueryResponses();
}
