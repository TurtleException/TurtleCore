package de.turtle_exception.core.client.internal;

import de.turtle_exception.core.client.api.TurtleClient;
import de.turtle_exception.core.client.api.requests.Action;
import de.turtle_exception.core.client.api.requests.ActionFuture;
import de.turtle_exception.core.client.api.requests.Request;
import de.turtle_exception.core.netcore.net.message.Message;
import de.turtle_exception.core.netcore.net.route.Route;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class ActionImpl<T> implements Action<T> {
    private final @NotNull TurtleClientImpl client;
    private final Route route;
    private final BiFunction<Message, Request<T>, T> handler;

    private Consumer<? super T>         onSuccess = null;
    private Consumer<? super Throwable> onFailure = null;

    private boolean priority = false;
    private long deadline = 0;

    public ActionImpl(@NotNull TurtleClient client, Route route, BiFunction<Message, Request<T>, T> handler) {
        this.client = (TurtleClientImpl) client;
        this.route = route;
        this.handler = handler;
    }

    @Override
    public @NotNull TurtleClient getClient() {
        return this.client;
    }

    @Override
    public @NotNull Action<T> deadline(long timestamp) {
        this.deadline = timestamp;
        return this;
    }

    @Override
    public void queue(@Nullable Consumer<? super T> success, @Nullable Consumer<? super Throwable> failure) {
        client.getNetClient().request(new Request<>(client, this, success, failure, route, deadline, priority));
    }

    @Override
    public @NotNull CompletableFuture<T> submit() {
        return new ActionFuture<>(this, deadline, priority, route);
    }

    public @NotNull ActionImpl<T> setPriority() {
        this.priority = true;
        return this;
    }

    public ActionImpl<T> onSuccess(Consumer<? super T> consumer) {
        this.onSuccess = consumer;
        return this;
    }

    public ActionImpl<T> onFailure(Consumer<? super Throwable> consumer) {
        this.onFailure = consumer;
        return this;
    }

    public void handleResponse(Message response, Request<T> request) {
        if (handler == null) {
            onSuccess.accept(null);
            request.onSuccess(null);
        } else {
            T obj = handler.apply(response, request);
            onSuccess.accept(obj);
            request.onSuccess(obj);
        }
    }
}
