package de.turtle_exception.client.api.request;

import com.google.gson.JsonObject;
import de.turtle_exception.client.internal.net.Connection;
import de.turtle_exception.client.internal.net.message.Message;
import de.turtle_exception.client.internal.net.message.Route;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public interface NetAction<T> extends Action<T> {
    @NotNull Connection getConnection();

    @NotNull Route getRoute();

    @NotNull JsonObject getJson();

    @NotNull Message buildMessage() throws Exception;

    @Override
    default @NotNull CompletableFuture<T> submit() {
        CompletableFuture<T> future = new CompletableFuture<>();

        // TODO: proofread docs
        // TODO: handle exception

        getConnection().send(this.buildMessage()).whenComplete((message, throwable) -> {
            if (throwable != null)
                future.completeExceptionally(throwable);

            try {
                future.complete(this.createResult(message));
            } catch (Throwable t) {
                future.completeExceptionally(t);
            }
        });

        return future;
    }

    @NotNull T createResult(@NotNull Message response) throws Exception;
}
