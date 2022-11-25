package de.turtle_exception.client.internal.util;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/**
 * A simple Thread that continuously repeats executing a {@link Runnable} until it is interrupted by a provided
 * {@link BooleanSupplier} or via {@link Thread#interrupt()}.
 */
public class Worker extends Thread {
    private final BooleanSupplier condition;
    private final Runnable task;
    private final long delay;

    private final Consumer<InterruptedException> exceptionHandler;

    public Worker(@NotNull BooleanSupplier condition, @NotNull Runnable task) {
        this(condition, task, 0, (Consumer<InterruptedException>) null);
    }

    public Worker(@NotNull BooleanSupplier condition, @NotNull Runnable task, long duration, @NotNull TimeUnit unit) {
        this(condition, task, duration, unit, null);
    }

    public Worker(@NotNull BooleanSupplier condition, @NotNull Runnable task, long duration, @NotNull TimeUnit unit, Consumer<InterruptedException> exceptionHandler) {
        this(condition, task, unit.toMillis(duration), exceptionHandler);
    }

    public Worker(@NotNull BooleanSupplier condition, @NotNull Runnable task, long delayMillis, Consumer<InterruptedException> exceptionHandler) {
        this.condition = condition;
        this.task = task;
        this.delay = delayMillis;

        this.exceptionHandler = exceptionHandler != null ? exceptionHandler : t -> { };

        this.setDaemon(true);
        this.start();
    }

    @Override
    public void run() {
        while (this.condition.getAsBoolean() && !this.isInterrupted()) {
            this.task.run();

            try {
                // TODO: don't busy-wait
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                this.exceptionHandler.accept(e);
            }
        }
    }
}
