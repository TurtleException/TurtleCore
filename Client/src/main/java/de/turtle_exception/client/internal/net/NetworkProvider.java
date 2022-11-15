package de.turtle_exception.client.internal.net;

import com.google.gson.JsonObject;
import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.Provider;
import de.turtle_exception.client.internal.data.Data;
import de.turtle_exception.client.internal.request.NetAction;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.AnnotationFormatError;

@SuppressWarnings("CodeBlock2Expr")
public class NetworkProvider extends Provider {
    private final Connection connection;

    public NetworkProvider(@NotNull Connection connection) {
        super(4);
        this.connection = connection;
    }

    @Override
    public <T extends Turtle> @NotNull Action<Boolean> delete(@NotNull Class<T> type, long id) throws AnnotationFormatError {
        return new NetAction<>(this, connection, Data.buildDelete(type, id), (request, response) -> {
            // this point should not be reached unless the response is a successful REMOVE call
            return true;
        });
    }

    @Override
    public <T extends Turtle> @NotNull Action<JsonObject> get(@NotNull Class<T> type, long id) throws AnnotationFormatError {
        return new NetAction<>(this, connection, Data.buildGet(type, id), (request, response) -> {
            return response.getData().content();
        });
    }

    @Override
    public <T extends Turtle> @NotNull Action<JsonObject> put(@NotNull Class<T> type, @NotNull JsonObject content) throws AnnotationFormatError {
        return new NetAction<>(this, connection, Data.buildPut(type, content.deepCopy()), (request, response) -> {
            return response.getData().content();
        });
    }

    @Override
    public <T extends Turtle> @NotNull Action<JsonObject> patch(@NotNull Class<T> type, @NotNull JsonObject content, long id) throws AnnotationFormatError {
        return new NetAction<>(this, connection, Data.buildPatch(type, content), (request, response) -> {
            return response.getData().content();
        });
    }
}
