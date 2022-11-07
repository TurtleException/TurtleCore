package de.turtle_exception.client.internal.request;

import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.request.DataAction;
import de.turtle_exception.client.internal.net.message.DataMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class DataActionImpl<T> implements DataAction<T> {
    protected final @NotNull TurtleClient client;
    protected final @NotNull DataMethod method;
    protected final @NotNull Class<T> type;
    protected final @Nullable JsonObject content;

    protected DataActionImpl(@NotNull TurtleClient client, @NotNull DataMethod method, @NotNull Class<T> type, @Nullable JsonObject content) {
        this.client = client;
        this.method = method;
        this.type = type;
        this.content = content;
    }

    @Override
    public @NotNull TurtleClient getClient() {
        return client;
    }

    @Override
    public @NotNull DataMethod getMethod() {
        return method;
    }

    @Override
    public @NotNull Class<T> getContentType() {
        return type;
    }

    @Override
    public @Nullable JsonObject getContent() {
        return content;
    }
}
