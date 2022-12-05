package de.turtle_exception.client.api.request.entities.form;

import de.turtle_exception.client.api.entities.form.ContentType;
import de.turtle_exception.client.api.entities.form.QueryElement;
import de.turtle_exception.client.api.request.Action;

@SuppressWarnings("unused")
public interface QueryElementAction extends Action<QueryElement> {
    QueryElementAction setTitle(String title);

    QueryElementAction setDescription(String description);

    QueryElementAction setContentType(ContentType contentType);
}
