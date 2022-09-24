package de.turtle_exception.core.netcore.net;

import de.turtle_exception.core.netcore.net.message.Message;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.*;

public class RequestExecutor {
    private final ScheduledThreadPoolExecutor executor;

    public RequestExecutor(RejectedExecutionHandler rejectedExecutionHandler) {
        this.executor = new ScheduledThreadPoolExecutor(4, rejectedExecutionHandler);
    }

    public void shutdown() {
        executor.shutdown();
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isTerminated() {
        return executor.isTerminated();
    }

    /**
     * Submits a {@link NetworkTask} to the underlying {@link Executor} and handles timeouts and returns a
     * {@link CompletableFuture} of type {@code R} as described in {@link NetworkTask#handleResponse(String)}.e
     * @param task An implementation of NetworkTask that should be executed.
     * @return Result of the execution.
     * @param <R> Type of the result provided by {@link NetworkTask#handleResponse(String)}
     */
    public @NotNull CompletableFuture<Message> submit(@NotNull Message task) throws CompletionException {
        final long timeout = System.currentTimeMillis() + task.getTimeout();

        return CompletableFuture.supplyAsync(() -> {
            // await response
            while (System.currentTimeMillis() <  timeout) {
                String response = callbacks.get(task.getCallbackCode());
                if (response != null)
                    return task.handleResponse(response);
            }
            throw new CompletionException(new TimeoutException());
        }, executor);
    }

    /* - - - */

    private final ConcurrentHashMap<Integer, String> callbacks = new ConcurrentHashMap<>();

    public void respond(int callbackCode, @NotNull String msg) {
        callbacks.put(callbackCode, msg);
    }
}
