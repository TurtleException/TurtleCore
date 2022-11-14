package de.turtle_exception.client.internal;

import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.Provider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

public class ActionImpl<T> implements Action<T> {
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

    @NotNull Callable<T> asCallable() {
        // TODO
    }
}
