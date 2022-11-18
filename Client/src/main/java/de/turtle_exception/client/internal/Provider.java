package de.turtle_exception.client.internal;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.internal.data.DataUtil;
import de.turtle_exception.client.internal.util.Worker;
import de.turtle_exception.client.internal.util.logging.NestedLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.lang.annotation.AnnotationFormatError;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Level;

/** Source of data */
public abstract class Provider {
    protected TurtleClientImpl client;
    protected NestedLogger logger;

    public enum Status { INIT, RUNNING, STOPPING, STOPPED }
    private @NotNull Status status = Status.INIT;

    private final LinkedBlockingQueue<Runnable>  defaultCallbacks = new LinkedBlockingQueue<>();
    private final LinkedBlockingQueue<Runnable> priorityCallbacks = new LinkedBlockingQueue<>();
    private final Worker[] workers;

    protected Provider(@Range(from = 1, to = Integer.MAX_VALUE) int workerSize) {
        this.logger.log(Level.INFO, "Allocating " + workerSize + " Workers.");

        this.workers = new Worker[workerSize];

        for (int i = 0; i < workerSize; i++) {
            workers[i] = new Worker(() -> status != Status.STOPPED, () -> {
                Runnable task = priorityCallbacks.poll();

                if (task == null)
                    task = defaultCallbacks.poll();

                if (task != null)
                    task.run();
            });
        }

        this.status = Status.RUNNING;
    }

    /* - - - */

    public abstract <T extends Turtle> @NotNull ActionImpl<Boolean> delete(@NotNull Class<T> type, long id) throws AnnotationFormatError;

    public <T extends Turtle> @NotNull ActionImpl<Boolean> delete(@NotNull T turtle) throws AnnotationFormatError {
        return this.delete(turtle.getClass(), turtle.getId());
    }

    public abstract <T extends Turtle> @NotNull ActionImpl<JsonObject> get(@NotNull Class<T> type, long id) throws AnnotationFormatError;

    public abstract <T extends Turtle> @NotNull ActionImpl<JsonArray> get(@NotNull Class<T> type) throws AnnotationFormatError;

    public abstract <T extends Turtle> @NotNull ActionImpl<JsonObject> put(@NotNull Class<T> type, @NotNull JsonObject content) throws AnnotationFormatError;

    public abstract <T extends Turtle> @NotNull ActionImpl<JsonObject> patch(@NotNull Class<T> type, @NotNull JsonObject content, long id) throws AnnotationFormatError;

    public <T extends Turtle> @NotNull ActionImpl<JsonObject> patch(@NotNull T turtle, @NotNull String key, @NotNull Object obj) throws AnnotationFormatError {
        JsonObject json = new JsonObject();
        DataUtil.addValue(json, key, obj);
        return this.patch(turtle.getClass(), json, turtle.getId());
    }

    public abstract <T extends Turtle> @NotNull ActionImpl<JsonObject> patchEntryAdd(@NotNull Class<T> type, long id, @NotNull String key, @NotNull Object obj);

    public <T extends Turtle> @NotNull ActionImpl<JsonObject> patchEntryAdd(@NotNull T turtle, @NotNull String key, @NotNull Object obj) {
        return this.patchEntryAdd(turtle.getClass(), turtle.getId(), key, obj);
    }

    public abstract <T extends Turtle> @NotNull ActionImpl<JsonObject> patchEntryDel(@NotNull Class<T> type, long id, @NotNull String key, @NotNull Object obj);

    public <T extends Turtle> @NotNull ActionImpl<JsonObject> patchEntryDel(@NotNull T turtle, @NotNull String key, @NotNull Object obj) {
        return this.patchEntryDel(turtle.getClass(), turtle.getId(), key, obj);
    }

    /* - - - */

    final void setClient(@NotNull TurtleClientImpl client) {
        this.client = client;
        this.logger = new NestedLogger("Provider", client.getLogger());
    }

    public @NotNull TurtleClient getClient() {
        return this.client;
    }

    /* - - - */

    <T> @NotNull CompletableFuture<T> submit(@NotNull ActionImpl<T> action) {
        if (status != Status.RUNNING)
            return CompletableFuture.failedFuture(new RejectedExecutionException("Provider is not running"));

        CompletableFuture<T> future = new CompletableFuture<>();

        Runnable task = () -> {
            try {
                T result = action.getCallable().call();
                future.complete(result);
            } catch (Throwable t) {
                future.completeExceptionally(t);
            }
        };

        if (action.isPriority())
            priorityCallbacks.add(task);
        else
            defaultCallbacks.add(task);

        this.logger.log(Level.FINER, "Action submitted: " + action.getClass().getSimpleName());

        return future;
    }

    /* - - - */

    public final void shutdown() {
        logger.log(Level.INFO, "Shutting down...");
        this.status = Status.STOPPING;

        final long timeout = System.currentTimeMillis() + 10000;

        logger.log(Level.INFO, "Awaiting termination of " + workers.length + " worker(s)");

        outer:
        while (true) {
            if (System.currentTimeMillis() >= timeout) {
                logger.log(Level.WARNING, "Timed out. Attempting to force shutdown.");
                this.shutdownNow();
                return;
            }

            for (Worker worker : this.workers)
                if (worker.isAlive())
                    continue outer;
            break;
        }

        this.status = Status.STOPPED;
        logger.log(Level.INFO, "OK bye.");
    }

    public final void shutdownNow() {
        logger.log(Level.INFO, "Forcing shutdown...");
        this.status = Status.STOPPING;

        logger.log(Level.WARNING, "Interrupting " + workers.length + " worker(s)");

        for (Worker worker : this.workers)
            worker.interrupt();

        this.status = Status.STOPPED;
        logger.log(Level.INFO, "OK bye.");
    }

    public final int getWorkerAmount() {
        return workers.length;
    }
}
