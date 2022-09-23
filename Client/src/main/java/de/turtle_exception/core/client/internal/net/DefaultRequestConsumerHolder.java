package de.turtle_exception.core.client.internal.net;

import de.turtle_exception.core.api.net.Action;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * A class that provides default {@link Consumer Consumers} for {@link Action Requests} (success & failure).
 * <p>Note: I admit this is an inelegant solution, but it works for now and I don't intend to change it unless it
 * becomes necessary.
 */
public interface DefaultRequestConsumerHolder {
    @NotNull Consumer<Object> getDefaultOnSuccess();

    void setDefaultOnSuccess(@NotNull Consumer<Object> c);

    @NotNull Consumer<? super Throwable> getDefaultOnFailure();

    void setDefaultOnFailure(@NotNull Consumer<? super Throwable> c);
}
