package de.turtle_exception.client.internal.net;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.internal.ActionImpl;
import de.turtle_exception.client.internal.Provider;
import de.turtle_exception.client.internal.data.Data;
import de.turtle_exception.client.internal.request.actions.NetAction;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.AnnotationFormatError;
import java.util.logging.Level;

@SuppressWarnings("CodeBlock2Expr")
public class NetworkProvider extends Provider {
    private Connection connection;

    public NetworkProvider(int workers) {
        super(workers);
    }

    public void setConnection(@NotNull Connection connection) {
        this.connection = connection;
        this.logger.log(Level.FINE, "Connection set!");
    }

    @Override
    public <T extends Turtle> @NotNull NetAction<Boolean> delete(@NotNull Class<T> type, long id) throws AnnotationFormatError {
        this.logger.log(Level.FINER, "DELETE request for id " + id);
        return new NetAction<>(this, connection, Data.buildDelete(type, id), (request, response) -> {
            // this point should not be reached unless the response is a successful REMOVE call
            return true;
        });
    }

    @Override
    public <T extends Turtle> @NotNull NetAction<JsonObject> get(@NotNull Class<T> type, long id) throws AnnotationFormatError {
        this.logger.log(Level.FINER, "GET request for id " + id);
        return new NetAction<>(this, connection, Data.buildGet(type, id), (request, response) -> {
            return response.getData().contentObject();
        });
    }

    @Override
    public <T extends Turtle> @NotNull NetAction<JsonArray> get(@NotNull Class<T> type) throws AnnotationFormatError {
        this.logger.log(Level.FINER, "GET request for type " + type.getSimpleName());
        return new NetAction<>(this, connection, Data.buildGet(type), (request, response) -> {
            return response.getData().contentArray();
        });
    }

    @Override
    public <T extends Turtle> @NotNull NetAction<JsonObject> put(@NotNull Class<T> type, @NotNull JsonObject content) throws AnnotationFormatError {
        this.logger.log(Level.FINER, "PUT request for object of type " + type.getSimpleName());
        return new NetAction<>(this, connection, Data.buildPut(type, content.deepCopy()), (request, response) -> {
            return response.getData().contentObject();
        });
    }

    @Override
    public <T extends Turtle> @NotNull NetAction<JsonObject> patch(@NotNull Class<T> type, @NotNull JsonObject content, long id) throws AnnotationFormatError {
        this.logger.log(Level.FINER, "PATCH request for id " + id);
        content.addProperty("id", id);
        return new NetAction<>(this, connection, Data.buildPatch(type, content), (request, response) -> {
            return response.getData().contentObject();
        });
    }

    @Override
    public @NotNull <T extends Turtle> ActionImpl<JsonObject> patchEntryAdd(@NotNull Class<T> type, long id, @NotNull String key, @NotNull Object obj) {
        this.logger.log(Level.FINER, "PATCH_ENTRY_ADD request for array \"" + key + "\" in id " + id);
        return new NetAction<>(this, connection, Data.buildPatchEntryAdd(type, id, key, obj), (request, response) -> {
            return response.getData().contentObject();
        });
    }

    @Override
    public @NotNull <T extends Turtle> ActionImpl<JsonObject> patchEntryDel(@NotNull Class<T> type, long id, @NotNull String key, @NotNull Object obj) {
        this.logger.log(Level.FINER, "PATCH_ENTRY_DEL request for array \"" + key + "\" in id " + id);
        return new NetAction<>(this, connection, Data.buildPatchEntryDel(type, id, key, obj), (request, response) -> {
            return response.getData().contentObject();
        });
    }
}
