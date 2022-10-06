package de.turtle_exception.client.api.requests;

import de.turtle_exception.client.api.event.net.ResponseEvent;
import de.turtle_exception.client.internal.ActionImpl;
import de.turtle_exception.client.internal.TurtleClientImpl;
import de.turtle_exception.core.net.message.InboundMessage;
import de.turtle_exception.core.net.route.CompiledRoute;
import de.turtle_exception.core.net.route.RouteError;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.logging.Level;

// TODO: docs
@SuppressWarnings("unused")
public class Request<T> {
    private final TurtleClientImpl client;
    private final ActionImpl<T> action;

    // these are the consumers provided by Action#queue()
    private final Consumer<? super T>         onSuccess;
    private final Consumer<? super Throwable> onFailure;

    private final CompiledRoute route;
    private final long    deadline;
    private final boolean priority;

    private boolean done      = false;
    private boolean cancelled = false;

    public Request(TurtleClientImpl client, ActionImpl<T> action, Consumer<? super T> onSuccess, Consumer<? super Throwable> onFailure, CompiledRoute route, long deadline, boolean priority) {
        this.client = client;
        this.action = action;
        this.onSuccess = onSuccess;
        this.onFailure = onFailure;
        this.route = route;
        this.deadline = deadline;
        this.priority = priority;
    }

    public @NotNull TurtleClientImpl getClient() {
        return this.client;
    }

    public void onSuccess(T successObj) {
        if (done) return;
        done = true;

        this.client.getCallbackExecutor().execute(() -> {
            try {
                onSuccess.accept(successObj);
            } catch (Throwable t) {
                client.getLogger().log(Level.WARNING, "Unexpected error when processing success object.", t);
            }
        });
    }

    public void onFailure(RouteError error) {
        this.onFailure(new RouteErrorException(error));
    }

    public void onFailure(Throwable t) {
        if (done) return;
        done = true;

        this.client.getCallbackExecutor().execute(() -> {
            try {
                onFailure.accept(t);
            } catch (Throwable th) {
                client.getLogger().log(Level.WARNING, "Unexpected error when processing failure.", th);
            }
        });
    }

    public void onCancelled() {
        this.onFailure(new CancellationException("Action has been cancelled"));
    }

    public void onTimeout() {
        this.onFailure(new TimeoutException("Action has timed out"));
    }

    public @NotNull Action<T> getAction() {
        return this.action;
    }

    public @NotNull Consumer<? super T> getOnSuccess() {
        return this.onSuccess;
    }

    public @NotNull Consumer<? super Throwable> getOnFailure() {
        return this.onFailure;
    }

    public boolean isPriority() {
        return this.priority;
    }

    public boolean isTimeout() {
        return deadline > 0 && deadline < System.currentTimeMillis();
    }

    public void cancel() {
        this.cancelled = true;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public CompiledRoute getRoute() {
        return route;
    }

    public long getDeadline() {
        return deadline;
    }

    /* - - - */

    public void handleResponse(@NotNull InboundMessage response) {
        action.handleResponse(response, this);
        client.getEventManager().handleEvent(new ResponseEvent(this, response));
    }
}
