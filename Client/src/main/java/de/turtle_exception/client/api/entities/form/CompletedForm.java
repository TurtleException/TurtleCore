package de.turtle_exception.client.api.entities.form;

import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.data.annotations.*;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * A CompletedForm is a {@link User} submission of a {@link TemplateForm} with custom (user-provided)
 * {@link QueryResponse QueryResponses}. Technically a CompletedForm can be submitted multiple times by the same user,
 * though that may be blocked by client applications controlling form submissions.
 */
@Resource(path = "forms_completed", builder = "buildCompletedForm")
@SuppressWarnings("unused")
public interface CompletedForm extends Turtle {
    @Override
    default @NotNull Action<CompletedForm> update() {
        return this.getClient().retrieveTurtle(this.getId(), CompletedForm.class);
    }

    /* - FORM - */

    /**
     * Provides the initial {@link TemplateForm} of this CompletedForm. A TemplateForm may have multiple CompletedForms.
     * @return The initial TemplateForm.
     */
    @Key(name = Keys.Form.CompletedForm.FORM, relation = Relation.MANY_TO_ONE, sqlType = Types.Form.CompletedForm.FORM)
    long getFormId();

    default TemplateForm getForm() {
        return this.getClient().getTurtleById(this.getFormId(), TemplateForm.class);
    }

    /* - AUTHOR - */

    /**
     * Provides the author of this CompletedForm.
     * <p> Technically a CompletedForm can be submitted multiple times by the same user, though that may be blocked by
     * client applications controlling form submissions.
     * @return The Author of this CompletedForm.
     */
    @Key(name = Keys.Form.CompletedForm.AUTHOR, sqlType = Types.Form.CompletedForm.AUTHOR)
    long getAuthorId();

    default User getAuthor() {
        return this.getClient().getTurtleById(this.getAuthorId(), User.class);
    }

    /* - SUBMISSION TIME - */

    /**
     * Provides the time this CompletedForm was submitted as UNIX epoch millis.
     * @return Submission time of this CompletedForm.
     */
    @Key(name = Keys.Form.CompletedForm.TIME_SUBMISSION, sqlType = Types.Form.CompletedForm.TIME_SUBMISSION)
    long getSubmissionTimeMillis();

    /**
     * Provides the time this CompletedForm was submitted as an {@link Instant}.
     * @return Submission time of this CompletedForm.
     */
    default @NotNull Instant getSubmissionTime() {
        return Instant.ofEpochMilli(this.getSubmissionTimeMillis());
    }

    /* - QUERY RESPONSES - */

    /**
     * Provides the {@link QueryResponse QueryResponses} of this CompletedForm. The contents of these objects are
     * provided by the user (author of this CompletedForm).
     * @return QueryResponses of this CompletedForm.
     */
    @Key(name = Keys.Form.CompletedForm.QUERY_RESPONSES, relation = Relation.ONE_TO_MANY, sqlType = Types.Form.CompletedForm.QUERY_RESPONSES)
    @Relational(table = "form_query_responses", self = "form", foreign = "query_response", type = Long.class)
    @NotNull List<Long> getQueryResponseIds();

    default @NotNull List<QueryResponse> getQueryResponses() {
        return this.getClient().getTurtles(QueryResponse.class, this.getQueryResponseIds());
    }
}
