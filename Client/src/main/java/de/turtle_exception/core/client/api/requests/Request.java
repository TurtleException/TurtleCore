package de.turtle_exception.core.client.api.requests;

import de.turtle_exception.core.client.internal.ActionImpl;
import de.turtle_exception.core.client.internal.TurtleClientImpl;
import de.turtle_exception.core.netcore.net.message.Message;
import de.turtle_exception.core.netcore.net.route.Route;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

// TODO: docs
public class Request<T> {
    private final TurtleClientImpl client;
    private final ActionImpl<T> action;
    private final Consumer<? super T>         onSuccess;
    private final Consumer<? super Throwable> onFailure;
    private final Route route;
    private final long    deadline;
    private final boolean priority;

    private boolean done      = false;
    private boolean cancelled = false;

    public Request(TurtleClientImpl client, ActionImpl<T> action, Consumer<? super T> onSuccess, Consumer<? super Throwable> onFailure, Route route, long deadline, boolean priority) {
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
        // TODO
    }

    public void onFailure(Message response) {
        // TODO
    }

    public void onFailure(Throwable t) {
        if (done) return;
        done = true;
        // TODO
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

    public Route getRoute() {
        return route;
    }

    public long getDeadline() {
        return deadline;
    }

    /* - - - */

    public void handleResponse(@NotNull Message response) {
        action.handleResponse(response, this);
        // TODO: event system
        //client.handleEvent(new NetRequestEvent(this, response));
    }
}
