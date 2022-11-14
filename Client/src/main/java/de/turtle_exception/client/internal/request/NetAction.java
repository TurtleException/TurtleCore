package de.turtle_exception.client.internal.request;

import de.turtle_exception.client.internal.ActionImpl;
import de.turtle_exception.client.internal.Provider;
import de.turtle_exception.client.internal.data.Data;
import de.turtle_exception.client.internal.net.Connection;
import kotlin.NotImplementedError;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;
import java.util.function.BiFunction;

public class NetAction<T> extends ActionImpl<T> {
    protected @NotNull Connection connection;
    protected @NotNull Data data;

    protected @NotNull BiFunction<Request, Response, T> finalizer;

    public NetAction(@NotNull Provider provider, @NotNull Connection connection, @NotNull Data data, @NotNull BiFunction<Request, Response, T> finalizer) {
        super(provider);

        this.connection = connection;
        this.data = data;
        this.finalizer = finalizer;
    }

    /* - - - */

    @Override
    protected @NotNull Callable<T> asCallable() throws IllegalStateException {
        return () -> {
            Request   request  = new Request(connection, data);
            IResponse response = connection.send(request).join();

            if (response instanceof Response resp)
                return finalizer.apply(request, resp);
            if (response instanceof ErrorResponse err)
                throw new RemoteErrorException(err);
            throw new NotImplementedError("Unknown response type: " + response.getClass().getName());
        };
    }
}
