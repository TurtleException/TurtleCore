package de.turtle_exception.client.api.event.entities.form.query_element;

import de.turtle_exception.client.api.entities.form.QueryElement;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.fancyformat.FormatText;
import org.jetbrains.annotations.NotNull;

public class QueryElementUpdateDescriptionEvent extends QueryElementUpdateEvent<FormatText> {
    public QueryElementUpdateDescriptionEvent(@NotNull QueryElement entity, FormatText oldValue, FormatText newValue) {
        super(entity, Keys.Form.QueryElement.DESCRIPTION, oldValue, newValue);
    }
}
