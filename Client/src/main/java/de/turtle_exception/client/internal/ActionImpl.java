package de.turtle_exception.client.internal;

import de.turtle_exception.client.api.request.Action;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

public abstract class ActionImpl<T> implements Action<T> {
    protected final @NotNull Provider provider;

    protected boolean priority = false;

    public ActionImpl(@NotNull Provider provider) {
        this.provider = provider;
    }

    @Override
    public @NotNull Provider getProvider() {
        return provider;
    }

    @Override
    public @NotNull CompletableFuture<T> submit() {
        return provider.submit(this);
    }

    /* - BUILDER - */

    public ActionImpl<T> priority() {
        priority = true;
        return this;
    }

    /* - - - */

    public boolean isPriority() {
        return priority;
    }

    /* - - - */

    /** Proxy method, so that {@link ActionImpl#asCallable()} can be used by the {@link Provider}. */
    @NotNull Callable<T> getCallable() throws IllegalStateException {
        return this.asCallable();
    }

    protected abstract @NotNull Callable<T> asCallable() throws IllegalStateException;
}
