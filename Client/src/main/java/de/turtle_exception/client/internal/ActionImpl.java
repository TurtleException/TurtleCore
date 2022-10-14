package de.turtle_exception.client.internal;

import com.google.gson.JsonElement;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.requests.Action;
import de.turtle_exception.client.api.requests.ActionFuture;
import de.turtle_exception.client.api.requests.Request;
import de.turtle_exception.client.api.requests.ActionHandler;
import de.turtle_exception.core.net.message.InboundMessage;
import de.turtle_exception.core.net.route.CompiledRoute;
import de.turtle_exception.core.net.route.Route;
import de.turtle_exception.core.net.route.RouteError;
import de.turtle_exception.core.net.route.Routes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ActionImpl<T> implements Action<T> {
    private final @NotNull TurtleClientImpl client;
    protected ActionHandler<T> handler;

    protected Route route;
    protected @Nullable JsonElement content;
    protected @NotNull  Object[]    routeArgs;

    private Consumer<? super T>         onSuccess = null;
    private Consumer<? super Throwable> onFailure = null;

    private boolean priority = false;
    private long deadline = 0;

    public ActionImpl(@NotNull TurtleClient client, Route route, ActionHandler<T> handler) {
        this.client = (TurtleClientImpl) client;
        this.handler = handler;

        this.route = route;
        this.content = null;
        this.routeArgs = new Object[0];
    }

    public ActionImpl(@NotNull TurtleClient client, Route route) {
        this(client, route, null);
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
    public void queue(@Nullable Consumer<? super T> success, @Nullable Consumer<? super Throwable> failure) throws IllegalArgumentException {
        client.getNetClient().request(new Request<>(client, this, success, failure, compileRoute(), deadline, priority));
    }

    @Override
    public @NotNull CompletableFuture<T> submit() throws IllegalArgumentException {
        return new ActionFuture<>(this, deadline, priority, compileRoute());
    }

    protected @NotNull CompiledRoute compileRoute() throws IllegalArgumentException {
        return route.compile(content, routeArgs);
    }

    public @NotNull ActionImpl<T> setContent(@Nullable JsonElement content) {
        this.content = content;
        return this;
    }

    public @NotNull ActionImpl<T> setRouteArgs(@NotNull Object... routeArgs) {
        this.routeArgs = routeArgs;
        return this;
    }

    /**
     * Marks this Action as a priority.
     * @return This ActionImpl, useful for chaining.
     */
    public @NotNull ActionImpl<T> setPriority() {
        this.priority = true;
        return this;
    }

    /**
     * An optional Consumer that can accept a success object before it gets passed to the Request Consumer. This is
     * useful to process caching before allowing the user to process the object.
     * @return This ActionImpl, useful for chaining.
     */
    public ActionImpl<T> onSuccess(Consumer<? super T> consumer) {
        this.onSuccess = consumer;
        return this;
    }

    public void handleResponse(InboundMessage response, Request<T> request) {
        if (response.getRoute().isRoute(Routes.ERROR)) {
            try {
                request.onFailure(RouteError.of(response.getRoute().content()));
            } catch (IllegalArgumentException e) {
                request.onFailure(e);
            }
            return;
        }

        if (onSuccess == null)
            onSuccess = obj -> { };
        if (onFailure == null)
            onFailure = thr -> { };

        if (handler == null) {
            onSuccess.accept(null);
            request.onSuccess(null);
        } else {
            try {
                T obj = handler.handle(response, request);
                onSuccess.accept(obj);
                request.onSuccess(obj);
            } catch (Exception e) {
                onFailure.accept(e);
                request.onFailure(e);
            }
        }
    }
}
