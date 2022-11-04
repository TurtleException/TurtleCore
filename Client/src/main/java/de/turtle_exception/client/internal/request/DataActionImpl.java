package de.turtle_exception.client.internal.request;

import com.google.gson.JsonObject;
import de.turtle_exception.client.api.request.DataAction;
import de.turtle_exception.client.internal.net.Connection;
import de.turtle_exception.client.internal.net.message.DataMessage;
import de.turtle_exception.client.internal.net.message.DataMethod;
import de.turtle_exception.client.internal.net.message.Message;
import de.turtle_exception.client.internal.net.message.Route;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class DataActionImpl<T> extends ActionImpl<T> implements DataAction<T> {
    protected @NotNull DataMethod method;
    protected @NotNull Class<?> contentType;
    protected @NotNull JsonObject content;

    public DataActionImpl(@NotNull Connection connection, @NotNull DataMethod method, @NotNull Class<?> type, @NotNull JsonObject content) {
        super(connection, Route.DATA, DataMessage.wrapJson(method, type, content));
        this.method = method;
        this.contentType = type;
        this.content = content;
    }

    @Override
    protected @NotNull Message buildMessage() {
        this.prepareJson();
        return new DataMessage(connection, conversation, deadline, method, contentType, content);
    }

    @Override
    public @NotNull DataMethod getMethod() {
        return method;
    }

    @Override
    public @NotNull Class<?> getContentType() {
        return contentType;
    }

    @Override
    public @NotNull JsonObject getContent() {
        return content;
    }

    /* - - - */

    @Override
    public @NotNull DataActionImpl<T> onSuccess(@Nullable Consumer<? super T> successFinalizer) {
        super.onSuccess(successFinalizer);
        return this;
    }

    @Override
    public @NotNull DataActionImpl<T> onFailure(@Nullable Consumer<? super Throwable> failureFinalizer) {
        super.onFailure(failureFinalizer);
        return this;
    }
}
