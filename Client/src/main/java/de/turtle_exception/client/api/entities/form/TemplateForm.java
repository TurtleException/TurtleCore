package de.turtle_exception.client.api.entities.form;

import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.data.annotations.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/** A template for a Form that can then be submitted as a {@link CompletedForm} by a {@link User}. */
@Resource(path = "forms_template", builder = "buildTemplateForm")
@SuppressWarnings("unused")
public interface TemplateForm extends Turtle {
    @Override
    default @NotNull Action<TemplateForm> update() {
        return this.getClient().retrieveTurtle(this.getId(), TemplateForm.class);
    }

    /* - TITLE - */

    /**
     * Provides the title of this TemplateForm. Form titles are not guaranteed to be unique and rather function as a
     * description. Uniqueness can only be checked by {@link TemplateForm#getId()}.
     * @return The TemplateForm title.
     */
    @Key(name = Keys.Form.TemplateForm.TITLE, sqlType = Types.Form.TemplateForm.TITLE)
    @NotNull String getTitle();

    /* - QUERIES - */

    /**
     * Provides the {@link QueryElement QueryElements} of this TemplateForm.
     * @return QueryResponses of this CompletedForm.
     */
    @Key(name = Keys.Form.TemplateForm.QUERIES, relation = Relation.MANY_TO_MANY, sqlType = Types.Form.TemplateForm.QUERIES)
    @Relational(table = "form_queries", self = "form", foreign = "query", type = QueryElement.class)
    @NotNull List<QueryElement> getQueries();
}
