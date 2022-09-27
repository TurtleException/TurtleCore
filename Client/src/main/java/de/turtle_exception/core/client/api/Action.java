package de.turtle_exception.core.client.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

// TODO: docs
public interface Action<T> {
    @NotNull TurtleClient getClient();

    default @NotNull Action<T> setTimeout(@Range(from = 0, to = Long.MAX_VALUE) long timeout, @NotNull TimeUnit unit) {
        return deadline(System.currentTimeMillis() + unit.toMillis(timeout));
    }

    @NotNull Action<T> deadline(long timestamp);

    /* - - - */

    default void queue() {
        this.queue(null);
    }

    default void queue(@Nullable Consumer<? super T> success) {
        this.queue(success, null);
    }

    void queue(@Nullable Consumer<? super T> success, @Nullable Consumer<? super Throwable> failure);

    @NotNull CompletableFuture<T> submit();
}
