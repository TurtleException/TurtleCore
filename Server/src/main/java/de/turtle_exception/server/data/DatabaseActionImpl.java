package de.turtle_exception.server.data;

import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.internal.net.message.DataMethod;
import de.turtle_exception.client.internal.request.DataActionImpl;
import de.turtle_exception.client.internal.util.ExceptionalFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class DatabaseActionImpl<T> extends DataActionImpl<T> {
    private final @NotNull DatabaseProvider provider;
    private final @NotNull ExceptionalFunction<Void, T> function;

    public DatabaseActionImpl(@NotNull TurtleClient client, @NotNull DatabaseProvider provider, @NotNull DataMethod method, @NotNull Class<T> type, @NotNull JsonObject content, @NotNull ExceptionalFunction<Void, T> function) {
        super(client, method, type, content);
        this.provider = provider;
        this.function = function;
    }

    @Override
    public @NotNull CompletableFuture<T> submit() {
        return provider.submit(function);
    }
}
