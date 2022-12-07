package de.turtle_exception.client.api.entities.form;

import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.data.annotations.Key;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.internal.data.annotations.Types;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** A {@link TemplateForm} element. This is not a resource itself, but all implementations must be resources. */
@SuppressWarnings("unused")
public interface Element extends Turtle {
    @Override
    @NotNull Action<? extends Element> update();

    /**
     * Provides the title of this Element. Element titles are not guaranteed to be unique and rather function as a
     * description. Uniqueness can only be checked by {@link Element#getId()}.
     * @return The Element title (may be {@code null}).
     */
    @Key(name = Keys.Form.Element.TITLE, sqlType = Types.Form.Element.TITLE)
    @Nullable String getTitle();

    /**
     * Creates an Action with the instruction to modify this Element's title and change it to the provided String.
     * @param title New Element title.
     * @return Action that provides the modified {@link Element} or a subtype on completion.
     */
    @NotNull Action<? extends Element> modifyTitle(@Nullable String title);
}
