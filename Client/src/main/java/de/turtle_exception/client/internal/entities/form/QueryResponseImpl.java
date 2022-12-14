package de.turtle_exception.client.internal.entities.form;

import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.form.QueryElement;
import de.turtle_exception.client.api.entities.form.QueryResponse;
import de.turtle_exception.client.internal.entities.TurtleImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class QueryResponseImpl extends TurtleImpl implements QueryResponse {
    private final long query;
    private final String content;

    public QueryResponseImpl(@NotNull TurtleClient client, long id, long query, String content) {
        super(client, id);
        this.query = query;
        this.content = content;
    }

    @Override
    public synchronized @NotNull QueryResponseImpl handleUpdate(@NotNull JsonObject json) {
        // this resource is immutable
        return this;
    }

    /* - - - */

    @Override
    public long getQueryId() {
        return this.query;
    }

    @Override
    public @Nullable String getAsString() {
        return this.content;
    }
}
