package de.turtle_exception.client.api.request.entities.form;

import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.entities.form.CompletedForm;
import de.turtle_exception.client.api.entities.form.QueryResponse;
import de.turtle_exception.client.api.entities.form.TemplateForm;
import de.turtle_exception.client.api.request.Action;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;

@SuppressWarnings("unused")
public interface CompletedFormAction extends Action<CompletedForm> {
    CompletedFormAction setForm(long form);

    default CompletedFormAction setForm(@NotNull TemplateForm form) {
        return this.setForm(form.getId());
    }

    CompletedFormAction setAuthor(long user);

    default CompletedFormAction setAuthor(@NotNull User user) {
        return this.setAuthor(user.getId());
    }

    CompletedFormAction setSubmissionTime(long millis);

    default CompletedFormAction setSubmissionTime(@NotNull Instant instant) {
        return this.setSubmissionTime(instant.toEpochMilli());
    }

    CompletedFormAction setQueryResponseIds(@NotNull Collection<Long> queryResponses);

    default CompletedFormAction setQueryResponses(@NotNull Collection<QueryResponse> queryResponses) {
        return this.setQueryResponseIds(queryResponses.stream().map(QueryResponse::getId).toList());
    }

    default CompletedFormAction setQueryResponseIds(@NotNull Long... queryResponses) {
        return this.setQueryResponseIds(Arrays.asList(queryResponses));
    }

    default CompletedFormAction setQueryResponses(@NotNull QueryResponse... queryResponses) {
        return this.setQueryResponses(Arrays.asList(queryResponses));
    }

    CompletedFormAction addQueryResponseId(long queryResponse);

    default CompletedFormAction addQueryResponse(@NotNull QueryResponse queryResponse) {
        return this.addQueryResponseId(queryResponse.getId());
    }

    CompletedFormAction removeQueryResponseId(long queryAction);

    default CompletedFormAction removeQueryResponse(@NotNull QueryResponse queryResponse) {
        return this.removeQueryResponseId(queryResponse.getId());
    }
}
