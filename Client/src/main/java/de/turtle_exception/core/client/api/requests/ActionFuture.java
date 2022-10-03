package de.turtle_exception.core.client.api.requests;

import de.turtle_exception.core.client.internal.ActionImpl;
import de.turtle_exception.core.client.internal.TurtleClientImpl;
import de.turtle_exception.core.core.net.route.Route;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ActionFuture<T> extends CompletableFuture<T> {
    private final Request<T> request;

    public ActionFuture(final ActionImpl<T> action, final long deadline, final boolean priority, final @NotNull Route route) {
        this.request = new Request<>(((TurtleClientImpl) action.getClient()), action, this::complete, this::completeExceptionally, route, deadline, priority);

        ((TurtleClientImpl) action.getClient()).getNetClient().request(this.request);
    }

    public ActionFuture(final T t) {
        this.request = null;
        complete(t);
    }

    public ActionFuture(final Throwable t) {
        this.request = null;
        completeExceptionally(t);
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        if (this.request != null)
            this.request.cancel();

        return (!isDone() && !isCancelled()) && super.cancel(mayInterruptIfRunning);
    }
}
