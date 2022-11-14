package de.turtle_exception.client.internal.request;

import de.turtle_exception.client.internal.ActionImpl;
import de.turtle_exception.client.internal.Provider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;

public class SimpleAction<T> extends ActionImpl<T> {
    protected final @NotNull Callable<T> callable;

    public SimpleAction(@NotNull Provider provider, @NotNull Callable<T> callable) {
        super(provider);
        this.callable = callable;
    }

    @Override
    protected @NotNull Callable<T> asCallable() {
        return callable;
    }
}
