package de.turtle_exception.client.api.request;

import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.internal.Provider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Consumer;

/**
 * An Action is a request to a {@link Provider} that will be executed asynchronously and produces a result that can be
 * handled in multiple ways.
 * @param <T> Result type.
 */
public interface Action<T> {
    /**
     * Returns the Provider that is responsible for this Action's execution.
     * @return Responsible Provider.
     */
    @NotNull Provider getProvider();

    /**
     * Convenience method to get the TurtleClient of the responsible Provider.
     * <p> This is a shortcut for {@code Action.getProvider().getClient()}
     * @return TurtleClient instance.
     */
    default @NotNull TurtleClient getClient() {
        return this.getProvider().getClient();
    }

    /* - - - */

    /**
     * Submits this Action to the {@link Provider} to be executed asynchronously. The returned CompletableFuture can be
     * used to retrieve the (possibly exceptional) result.
     * @return CompletableFuture wrapping this Action's execution.
     */
    @NotNull CompletableFuture<T> submit();

    /**
     * Submits this Action to the {@link Provider} to be executed asynchronously.
     */
    default void queue() {
        this.queue(null);
    }

    /**
     * Submits this Action to the {@link Provider} to be executed asynchronously. If execution is successful, the result
     * will be fed to the consumer provided as the first argument.
     * @param success Consumer that accepts successful result.
     */
    default void queue(@Nullable Consumer<T> success) {
        this.queue(success, null);
    }

    /**
     * Submits this Action to the {@link Provider} to be executed asynchronously. If execution is successful, the result
     * will be fed to the consumer provided as the first argument. If not, the exception will be fed to the consumer
     * provided as the second argument.
     * @param success Consumer that accepts successful result.
     * @param failure Consumer that accepts exceptional result.
     */
    default void queue(@Nullable Consumer<T> success, @Nullable Consumer<Throwable> failure) {
        this.submit().whenComplete((result, throwable) -> {
            if (result != null && success != null)
                success.accept(result);
            if (throwable != null && failure != null)
                failure.accept(throwable);
        });
    }

    /**
     * Submits this Action to the {@link Provider} to be executed asynchronously and joins the thread awaiting its
     * completion. This method will either return the desired object of type T or throw an exception.
     * @return Requested object of type T
     * @throws CancellationException if the execution of this Action has been cancelled.
     * @throws CompletionException if the execution of this Action completed exceptionally or a completion computation
     *                             threw an exception.
     */
    @SuppressWarnings("UnusedReturnValue")
    default @NotNull T complete() throws CancellationException, CompletionException {
        return this.submit().join();
    }
}
