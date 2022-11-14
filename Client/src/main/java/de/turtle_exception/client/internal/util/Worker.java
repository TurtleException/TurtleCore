package de.turtle_exception.client.internal.util;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

/**
 * A simple Thread that continuously repeats executing a {@link Runnable} until it is interrupted by a provided
 * {@link BooleanSupplier} or via {@link Thread#interrupt()}.
 */
public class Worker extends Thread {
    private final BooleanSupplier condition;
    private final Runnable task;
    private final long delay;

    public Worker(@NotNull BooleanSupplier condition, @NotNull Runnable task) {
        this(condition, task, 0);
    }

    public Worker(@NotNull BooleanSupplier condition, @NotNull Runnable task, long duration, @NotNull TimeUnit unit) {
        this(condition, task, unit.toMillis(duration));
    }

    public Worker(@NotNull BooleanSupplier condition, @NotNull Runnable task, long delayMillis) {
        this.condition = condition;
        this.task = task;
        this.delay = delayMillis;
        this.setDaemon(true);
        this.start();
    }

    @Override
    public void run() {
        while (this.condition.getAsBoolean() && !this.isInterrupted()) {
            this.task.run();

            try {
                this.wait(delay);
            } catch (InterruptedException ignored) { /* TODO: this should probably be handled */ }
        }
    }
}
