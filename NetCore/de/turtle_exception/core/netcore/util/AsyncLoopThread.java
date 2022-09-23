package de.turtle_exception.core.netcore.util;

import java.util.function.BooleanSupplier;

/**
 * A simple Thread that continuously repeats executing a {@link Runnable} until it is interrupted by a provided
 * {@link BooleanSupplier} or via {@link Thread#interrupt()}.
 */
public class AsyncLoopThread extends Thread {
    private final BooleanSupplier condition;
    private final Runnable task;

    public AsyncLoopThread(BooleanSupplier condition, Runnable task) {
        this.condition = condition;
        this.task = task;
        this.setDaemon(true);
        this.start();
    }

    @Override
    public void run() {
        while (condition.getAsBoolean() && !this.isInterrupted()) {
            task.run();
        }
    }
}
