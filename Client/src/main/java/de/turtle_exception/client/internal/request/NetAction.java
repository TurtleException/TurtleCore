package de.turtle_exception.client.internal.request;

import com.google.gson.JsonObject;
import de.turtle_exception.client.internal.ActionImpl;
import de.turtle_exception.client.internal.Provider;
import de.turtle_exception.client.internal.net.Connection;
import de.turtle_exception.client.internal.net.DataMethod;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;

// TODO: complete
public class NetAction<T> extends ActionImpl<T> {
    protected @NotNull Connection connection;
    protected @NotNull DataMethod method;
    protected @NotNull Class<?>   type;
    protected @NotNull JsonObject content;

    public NetAction(@NotNull Provider provider, @NotNull Connection connection, @NotNull DataMethod method, @NotNull Class<?> type, @NotNull JsonObject content) {
        super(provider);

        this.connection = connection;
        this.method     = method;
        this.type       = type;
        this.content    = content;
    }

    /* - - - */

    @Override
    protected @NotNull Callable<T> asCallable() throws IllegalStateException {
        // TODO
    }
}
