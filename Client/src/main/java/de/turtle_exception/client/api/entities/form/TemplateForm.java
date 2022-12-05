package de.turtle_exception.client.api.entities.form;

import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.internal.data.annotations.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Resource(path = "forms_template", builder = "buildTemplateForm")
@SuppressWarnings("unused")
public interface TemplateForm extends Turtle {
    @Key(name = Keys.Form.TemplateForm.TITLE, sqlType = Types.Form.TemplateForm.TITLE)
    @NotNull String getTitle();

    @Key(name = Keys.Form.TemplateForm.QUERIES, relation = Relation.MANY_TO_MANY, sqlType = Types.Form.TemplateForm.QUERIES)
    @Relational(table = "form_queries", self = "form", foreign = "query", type = QueryElement.class)
    @NotNull List<QueryElement> getQueries();
}
