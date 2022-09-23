package de.turtle_exception.core.client.internal.util;

import de.turtle_exception.core.client.internal.net.client.InternalClient;
import de.turtle_exception.core.client.internal.net.server.InternalServer;

import java.util.function.BooleanSupplier;

/**
 * A simple Thread that continuously repeats executing a {@link Runnable} until it is interrupted by a provided
 * {@link BooleanSupplier} or via {@link Thread#interrupt()}.
 * @see InternalServer
 * @see InternalClient
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
