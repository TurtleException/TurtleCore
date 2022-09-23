package de.turtle_exception.core.internal.net;

import de.turtle_exception.core.api.net.Action;
import de.turtle_exception.core.internal.TurtleClientImpl;
import de.turtle_exception.core.internal.TurtleCore;
import de.turtle_exception.core.internal.TurtleServerImpl;
import de.turtle_exception.core.internal.net.server.VirtualClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

public abstract class ActionImpl<T> implements Action<T> {
    private final TurtleCore core;
    private final Route route;

    // only for server actions
    private final @Nullable VirtualClient target;

    private int priority = 0;

    private long timeout;

    private Consumer<T> onSuccess;
    private Consumer<Throwable> onFailure;

    public ActionImpl(@NotNull TurtleClientImpl core, @NotNull Route route) {
        this.core = core;
        this.route = route;
        this.target = null;
    }

    public ActionImpl(@NotNull TurtleServerImpl core, @NotNull Route route, @NotNull VirtualClient target) {
        this.core = core;
        this.route = route;
        this.target = target;
    }

    @Override
    public CompletableFuture<T> complete() throws RejectedExecutionException, CompletionException {
        try {
            return core.getNetworkAdapter().submitOutbound(this);
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
        return onSuccess != null ? onSuccess : core.getDefaultOnSuccess();
    }

    // TODO: call this method
    @NotNull Consumer<? super Throwable> getOnFailure() {
        return onFailure != null ? onFailure : core.getDefaultOnSuccess();
    }

    public @NotNull Route getRoute() {
        return route;
    }

    // only for server actions
    public @Nullable VirtualClient getTarget() {
        return target;
    }
}
