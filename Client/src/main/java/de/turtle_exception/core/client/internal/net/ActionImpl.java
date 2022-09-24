package de.turtle_exception.core.client.internal.net;

import de.turtle_exception.core.client.api.net.Action;
import de.turtle_exception.core.client.internal.TurtleClientImpl;
import de.turtle_exception.core.netcore.net.Route;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

public abstract class ActionImpl<T> implements Action<T> {
    private final TurtleClientImpl client;
    private final Route route;

    private int priority = 0;

    private long timeout;

    private Consumer<T> onSuccess;
    private Consumer<Throwable> onFailure;

    public ActionImpl(@NotNull TurtleClientImpl client, @NotNull Route route) {
        this.client = client;
        this.route = route;
    }

    @Override
    public CompletableFuture<T> complete() throws RejectedExecutionException, CompletionException {
        try {
            return client.getNetworkAdapter().submitOutbound(this);
        } catch (TimeoutException e) {
            throw new CompletionException(e);
        }
    }

    @Override
    public void queue() {

    }

    @Override
    public Action<T> onSuccess(Consumer<T> c) {
        this.onSuccess = c;
        return this;
    }

    @Override
    public Action<T> onFailure(Consumer<Throwable> c) {
        this.onFailure = c;
        return this;
    }

    @Override
    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public int getPriority() {
        return this.priority;
    }

    @Override
    public void setTimeout(@Range(from = 0, to = Long.MAX_VALUE) long ms) {
        this.timeout = ms;
    }

    @Override
    public long getTimeout() {
        return timeout;
    }

    // TODO: call this method
    @NotNull Consumer<? super T> getOnSuccess() {
        return onSuccess != null ? onSuccess : client.getDefaultOnSuccess();
    }

    // TODO: call this method
    @NotNull Consumer<? super Throwable> getOnFailure() {
        return onFailure != null ? onFailure : client.getDefaultOnSuccess();
    }

    public @NotNull Route getRoute() {
        return route;
    }
}
