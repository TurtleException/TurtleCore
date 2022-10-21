package de.turtle_exception.core.util;

import org.jetbrains.annotations.NotNull;

import java.util.function.BooleanSupplier;

@SuppressWarnings("unused")
public class Worker extends Thread {
    private final BooleanSupplier condition;
    private final Runnable task;

    public Worker(@NotNull BooleanSupplier condition, @NotNull Runnable task) {
        super();

        this.condition = condition;
        this.task = task;
    }

    @Override
    public void run() {
        while (this.condition.getAsBoolean() && !this.isInterrupted()) {
            this.task.run();
        }
    }
}
