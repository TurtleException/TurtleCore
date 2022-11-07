package de.turtle_exception.server.data;

import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.internal.net.message.DataMethod;
import de.turtle_exception.client.internal.request.DataActionImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

public class DatabaseActionImpl<T> extends DataActionImpl<T> {
    private final @NotNull DatabaseProvider provider;
    private final @NotNull Callable<T> callable;

    public DatabaseActionImpl(@NotNull TurtleClient client, @NotNull DatabaseProvider provider, @NotNull DataMethod method, @NotNull Class<T> type, @Nullable JsonObject content, @NotNull Callable<T> callable) {
        super(client, method, type, content);
        this.provider = provider;
        this.callable = callable;
    }

    public DatabaseActionImpl(@NotNull TurtleClient client, @NotNull DatabaseProvider provider, @NotNull DataMethod method, @NotNull Class<T> type, @NotNull Callable<T> callable) {
        this(client, provider, method, type, null, callable);
    }

    @Override
    public @NotNull CompletableFuture<T> submit() {
        return provider.submit(callable);
    }
}
