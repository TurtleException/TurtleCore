package de.turtle_exception.client.api.request;

import de.turtle_exception.client.api.TurtleClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Consumer;

public interface Action<T> {
    @NotNull TurtleClient getClient();

    /* - - - */

    @NotNull CompletableFuture<T> submit();

    default void queue() {
        this.queue(null);
    }

    default void queue(@Nullable Consumer<T> success) {
        this.queue(null, null);
    }

    void queue(@Nullable Consumer<T> success, @Nullable Consumer<Throwable> failure);

    default @NotNull T complete() throws CancellationException, CompletionException {
        return this.submit().join();
    }
}
