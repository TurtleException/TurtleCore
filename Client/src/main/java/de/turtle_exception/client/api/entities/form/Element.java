package de.turtle_exception.client.api.entities.form;

import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.data.annotations.Key;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.internal.data.annotations.Types;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public interface Element extends Turtle {
    @Key(name = Keys.Form.Element.TITLE, sqlType = Types.Form.Element.TITLE)
    @Nullable String getTitle();

    @NotNull Action<? extends Element> modifyTitle(@Nullable String title);
}
