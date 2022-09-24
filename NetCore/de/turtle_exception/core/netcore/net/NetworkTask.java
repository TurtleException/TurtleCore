package de.turtle_exception.core.netcore.net;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public interface NetworkTask<R> {
    long getTimeout();

    CompletableFuture<R> complete() throws CompletionException;

    R handleResponse(@NotNull String msg);

    int getCallbackCode();
}
