package de.turtle_exception.client.api.request.entities.form;

import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.entities.form.CompletedForm;
import de.turtle_exception.client.api.entities.form.QueryResponse;
import de.turtle_exception.client.api.entities.form.TemplateForm;
import de.turtle_exception.client.api.request.Action;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;

/**
 * A CompletedFormAction is an Action that requests the creation of a new {@link CompletedForm}, according to the
 * arguments this Action holds. If any required field is missing the server will reject the request and respond with an
 * error. Required fields are all attributes that are not a subclass of {@link Collection}, as these are set to an empty
 * Collection by default.
 * @see TurtleClient#createCompletedForm()
 */
@SuppressWarnings("unused")
public interface CompletedFormAction extends Action<CompletedForm> {
    /**
     * Sets the Form id of this CompletedForm to the provided id.
     * @param form CompletedForm Form id.
     * @return This CompletedFormAction for chaining convenience.
     */
    CompletedFormAction setForm(long form);

    /**
     * Sets the Form of this CompletedForm to the provided TemplateForm.
     * @param form CompletedForm Form.
     * @return This CompletedFormAction for chaining convenience.
     */
    default CompletedFormAction setForm(@NotNull TemplateForm form) {
        return this.setForm(form.getId());
    }

    /**
     * Sets the author id of this CompletedForm to the provided (user) id.
     * @param user CompletedForm author id.
     * @return This CompletedFormAction for chaining convenience.
     */
    CompletedFormAction setAuthor(long user);

    /**
     * Sets the author of this CompletedForm to the provided User.
     * @param user CompletedForm author.
     * @return This CompletedFormAction for chaining convenience.
     */
    default CompletedFormAction setAuthor(@NotNull User user) {
        return this.setAuthor(user.getId());
    }

    /**
     * Sets the submission time of this CompletedForm to the provided unix timestamp (milliseconds).
     * @param millis CompletedForm submission time as unix timestamp.
     * @return This CompletedFormAction for chaining convenience.
     */
    CompletedFormAction setSubmissionTime(long millis);

    /**
     * Sets the submission time of this CompletedForm to the provided Instant.
     * @param instant CompletedForm submission time.
     * @return This CompletedFormAction for chaining convenience.
     */
    default CompletedFormAction setSubmissionTime(@NotNull Instant instant) {
        return this.setSubmissionTime(instant.toEpochMilli());
    }

    /**
     * Sets the List of ids that each represent a {@link QueryResponse} that is part of this Form.
     * The existing List will be overwritten.
     * @param queryResponses Collection of QueryResponse ids.
     * @return This CompletedFormAction for chaining convenience.
     */
    CompletedFormAction setQueryResponseIds(@NotNull Collection<Long> queryResponses);

    /**
     * Sets the List of {@link QueryResponse QueryResponses} that are part of this Form.
     * The existing List will be overwritten.
     * @param queryResponses Collection of QueryResponses.
     * @return This CompletedFormAction for chaining convenience.
     */
    default CompletedFormAction setQueryResponses(@NotNull Collection<QueryResponse> queryResponses) {
        return this.setQueryResponseIds(queryResponses.stream().map(QueryResponse::getId).toList());
    }

    /**
     * Sets the List of ids that each represent a {@link QueryResponse} that is part of this Form.
     * The existing List will be overwritten.
     * @param queryResponses Array of QueryResponse ids.
     * @return This CompletedFormAction for chaining convenience.
     */
    default CompletedFormAction setQueryResponseIds(@NotNull Long... queryResponses) {
        return this.setQueryResponseIds(Arrays.asList(queryResponses));
    }

    /**
     * Sets the List of {@link QueryResponse QueryResponses} that are part of this Form.
     * The existing List will be overwritten.
     * @param queryResponses Array of QueryResponses.
     * @return This CompletedFormAction for chaining convenience.
     */
    default CompletedFormAction setQueryResponses(@NotNull QueryResponse... queryResponses) {
        return this.setQueryResponses(Arrays.asList(queryResponses));
    }

    /**
     * Adds the provided {@code long} to the List of ids that each represent a {@link QueryResponse} as a part of this
     * Form.
     * @param queryResponse QueryResponse id.
     * @return This CompletedFormAction for chaining convenience.
     */
    CompletedFormAction addQueryResponseId(long queryResponse);

    /**
     * Adds the provided {@link QueryResponse} to the List of QueryResponses that are part of this Form.
     * @param queryResponse Some QueryResponse.
     * @return This CompletedFormAction for chaining convenience.
     */
    default CompletedFormAction addQueryResponse(@NotNull QueryResponse queryResponse) {
        return this.addQueryResponseId(queryResponse.getId());
    }

    /**
     * Removes the provided {@code long} from the List of ids that each represent a {@link QueryResponse} as a part of
     * this Form.
     * @param queryResponse QueryResponse id.
     * @return This CompletedFormAction for chaining convenience.
     */
    CompletedFormAction removeQueryResponseId(long queryResponse);

    /**
     * Removes the provided {@link User} from the List of QueryResponses that are part of this Form.
     * @param queryResponse Some QueryResponse.
     * @return This CompletedFormAction for chaining convenience.
     */
    default CompletedFormAction removeQueryResponse(@NotNull QueryResponse queryResponse) {
        return this.removeQueryResponseId(queryResponse.getId());
    }
}
