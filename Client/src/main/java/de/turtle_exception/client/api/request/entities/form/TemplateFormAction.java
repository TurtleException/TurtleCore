package de.turtle_exception.client.api.request.entities.form;

import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.form.QueryElement;
import de.turtle_exception.client.api.entities.form.TemplateForm;
import de.turtle_exception.client.api.request.Action;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * A TemplateFormAction is an Action that requests the creation of a new {@link TemplateForm}, according to the
 * arguments this Action holds. If any required field is missing the server will reject the request and respond with an
 * error. Required fields are all attributes that are not a subclass of {@link Collection}, as these are set to an empty
 * Collection by default.
 * @see TurtleClient#createTemplateForm()
 */
@SuppressWarnings("unused")
public interface TemplateFormAction extends Action<TemplateForm> {
    /**
     * Sets the title of this TemplateForm to the provided String.
     * @param title TemplateForm title.
     * @return This TemplateFormAction for chaining convenience.
     */
    TemplateFormAction setTitle(String title);

    /**
     * Sets the List of ids that each represent a {@link QueryElement} that is part of this Form.
     * The existing List will be overwritten.
     * @param queries Collection of QueryElement ids.
     * @return This TemplateFormAction for chaining convenience.
     */
    TemplateFormAction setQueryIds(@NonNull List<Long> queries);

    /**
     * Sets the List of {@link QueryElement QueryElements} that are part of this Form.
     * The existing List will be overwritten.
     * @param queries Collection of QueryElements.
     * @return This TemplateFormAction for chaining convenience.
     */
    default TemplateFormAction setQueries(@NonNull List<QueryElement> queries) {
        return this.setQueryIds(queries.stream().map(QueryElement::getId).toList());
    }

    /**
     * Sets the List of ids that each represent a {@link QueryElement} that is part of this Form.
     * The existing List will be overwritten.
     * @param queries Array of QueryElement ids.
     * @return This TemplateFormAction for chaining convenience.
     */
    default TemplateFormAction setQueryIds(@NonNull Long... queries) {
        return this.setQueryIds(Arrays.asList(queries));
    }

    /**
     * Sets the List of {@link QueryElement QueryElements} that are part of this Form.
     * The existing List will be overwritten.
     * @param queries Array of QueryElements.
     * @return This TemplateFormAction for chaining convenience.
     */
    default TemplateFormAction setQueries(@NonNull QueryElement... queries) {
        return this.setQueries(Arrays.asList(queries));
    }

    /**
     * Adds the provided {@code long} to the List of ids that each represent a {@link QueryElement} as a part of this
     * Form.
     * @param query QueryElement id.
     * @return This TemplateFormAction for chaining convenience.
     */
    TemplateFormAction addQueryId(long query);

    /**
     * Adds the provided {@link QueryElement} to the List of QueryElements that are part of this Form.
     * @param query Some QueryElement.
     * @return This TemplateFormAction for chaining convenience.
     */
    default TemplateFormAction addQuery(@NonNull QueryElement query) {
        return this.addQueryId(query.getId());
    }

    /**
     * Removes the provided {@code long} from the List of ids that each represent a {@link QueryElement} as a part of
     * this Form.
     * @param query QueryElement id.
     * @return This TemplateFormAction for chaining convenience.
     */
    TemplateFormAction removeQueryId(long query);

    /**
     * Removes the provided {@link QueryElement} from the List of QueryElements that are part of this Form.
     * @param query Some QueryElement.
     * @return This TemplateFormAction for chaining convenience.
     */
    default TemplateFormAction removeQuery(@NonNull QueryElement query) {
        return this.removeQueryId(query.getId());
    }
}
