package de.turtle_exception.client.api.request.entities.form;

import de.turtle_exception.client.api.entities.form.QueryElement;
import de.turtle_exception.client.api.entities.form.TemplateForm;
import de.turtle_exception.client.api.request.Action;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public interface TemplateFormAction extends Action<TemplateForm> {
    TemplateFormAction setTitle(String title);

    TemplateFormAction setQueryIds(@NonNull List<Long> queries);

    default TemplateFormAction setQueries(@NonNull List<QueryElement> queries) {
        return this.setQueryIds(queries.stream().map(QueryElement::getId).toList());
    }

    default TemplateFormAction setQueryIds(@NonNull Long... queries) {
        return this.setQueryIds(Arrays.asList(queries));
    }

    default TemplateFormAction setQueries(@NonNull QueryElement... queries) {
        return this.setQueries(Arrays.asList(queries));
    }

    TemplateFormAction addQueryId(long query);

    default TemplateFormAction addQuery(@NonNull QueryElement query) {
        return this.addQueryId(query.getId());
    }

    TemplateFormAction removeQueryId(long query);

    default TemplateFormAction removeQuery(@NonNull QueryElement query) {
        return this.removeQueryId(query.getId());
    }
}
