package de.turtle_exception.core.client.internal;

import de.turtle_exception.core.client.api.requests.Action;
import de.turtle_exception.core.client.api.TurtleClient;
import de.turtle_exception.core.netcore.net.route.Route;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ActionImpl<T> implements Action<T> {
    private final @NotNull TurtleClientImpl client;
    private final Route route;

    private boolean priority = false;
    private long deadline = 0;

    public ActionImpl(@NotNull TurtleClient client, Route route) {
        this.client = (TurtleClientImpl) client;
        this.route = route;
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
        // TODO
    }

    @Override
    public @NotNull CompletableFuture<T> submit() {
        // TODO
    }

    public @NotNull ActionImpl<T> setPriority() {
        this.priority = true;
        return this;
    }
}
