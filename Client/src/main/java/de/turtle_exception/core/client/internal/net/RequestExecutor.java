package de.turtle_exception.core.client.internal.net;

import de.turtle_exception.core.client.internal.net.action.RemoteActionImpl;
import de.turtle_exception.core.client.internal.net.action.AnswerableAction;
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

    public <T> CompletableFuture<T> register(ActionImpl<T> request) {
        final long timeout = System.currentTimeMillis() + request.getTimeout();

        return CompletableFuture.supplyAsync(() -> {
            if (request instanceof AnswerableAction<T> aAction) {
                // await response
                while (System.currentTimeMillis() < timeout) {
                    // this will return null until aAction#handle(RemoteActionImpl) has been called
                    RemoteActionImpl response = aAction.getResponse();
                    if (response != null)
                        return aAction.handleResponse(response);
                }
                throw new CompletionException(new TimeoutException());
            }

            throw new IllegalArgumentException("Action of type " + request.getClass().getSimpleName() + " is not supported.");
        }, executor);
    }

    /* - - - */

    private final ConcurrentHashMap<Integer, RemoteActionImpl> callbacks = new ConcurrentHashMap<>();

    public void alertCallback(int callbackCode, @NotNull RemoteActionImpl action) {
        callbacks.put(callbackCode, action);
    }
}
