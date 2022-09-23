package de.turtle_exception.core.api.net;

import org.jetbrains.annotations.Range;

import java.util.concurrent.*;
import java.util.function.Consumer;

public interface Action<T> {
    CompletableFuture<T> complete() throws CompletionException;

    /**
     * Queues the Action to be completed (according to priority and rate limits) and blocks the calling thread until
     * completion. The result will be returned on success and errors will be thrown as {@link RuntimeException}.
     * @return Action result.
     * @throws RejectedExecutionException if the action execution has been rejected by the internal NetworkAdapter.
     * @throws CancellationException if the underlying {@link CompletableFuture} has been cancelled. This may happen
     *                               because the timeout has been exceeded.
     * @throws ExecutionException if the Action completed exceptionally.
     * @throws InterruptedException if the current thread was interrupted while waiting.
     */
    default T await() throws RejectedExecutionException, CancellationException, ExecutionException, InterruptedException {
        return complete().get();
    }

    /**
     * Queues the Action to be completed asynchronously.
     */
    void queue();

    /**
     * @param c Will accept the result on success.
     */
    Action<T> onSuccess(Consumer<T> c);

    /**
     * @param c Will accept the Throwable on failure.
     */
    Action<T> onFailure(Consumer<Throwable> c);

    void setPriority(int priority);

    int getPriority();

    /**
     * Sets the amount of milliseconds to wait after sending the request before a {@link TimeoutException} gets thrown.
     * @param ms The maximum time before the Action gets discarded.
     * @throws IllegalArgumentException if the provided value is out of the allowed range.
     */
    void setTimeout(@Range(from = 0, to = Long.MAX_VALUE) long ms);

    long getTimeout();
}
