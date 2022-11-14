package de.turtle_exception.client.api.request;

import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.internal.Provider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Consumer;

public interface Action<T> {
    @NotNull Provider getProvider();

    default @NotNull TurtleClient getClient() {
        return this.getProvider().getClient();
    }

    /* - - - */

    @NotNull CompletableFuture<T> submit();

    default void queue() {
        this.queue(null);
    }

    default void queue(@Nullable Consumer<T> success) {
        this.queue(null, null);
    }

    default void queue(@Nullable Consumer<T> success, @Nullable Consumer<Throwable> failure) {
        this.submit().whenComplete((result, throwable) -> {
            if (result != null && success != null)
                success.accept(result);
            if (throwable != null && failure != null)
                failure.accept(throwable);
        });
    }

    default @NotNull T complete() throws CancellationException, CompletionException {
        return this.submit().join();
    }
}
