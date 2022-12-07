package de.turtle_exception.client.api.entities.form;

import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.data.annotations.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

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

    /* - ELEMENTS - */

    /**
     * Provides the {@link Element Elements} of this TemplateForm.
     * @return Elements of this TemplateForm.
     */
    @Key(name = Keys.Form.TemplateForm.ELEMENTS, relation = Relation.MANY_TO_MANY, sqlType = Types.Form.TemplateForm.ELEMENTS)
    @Relational(table = "form_elements", self = "form", foreign = "element", type = Long.class)
    @NotNull List<Long> getElementIds();

    default @NotNull List<Element> getElements() {
        return this.getElements(Element.class);
    }

    default <T extends Element> @NotNull List<T> getElements(@NotNull Class<T> type) {
        return this.getClient().getTurtles(type, this.getElementIds());
    }
}
