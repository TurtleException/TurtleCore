package de.turtle_exception.client.internal.net;

import com.google.gson.JsonObject;
import de.turtle_exception.client.api.request.DataAction;
import de.turtle_exception.client.internal.Provider;
import de.turtle_exception.client.internal.data.DataUtil;
import de.turtle_exception.client.internal.data.annotations.Key;
import de.turtle_exception.client.internal.net.message.DataMethod;
import de.turtle_exception.client.internal.request.RemoteDataActionImpl;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.AnnotationFormatError;

public class NetworkProvider extends Provider {
    private final Connection connection;

    public NetworkProvider(@NotNull Connection connection) {
        this.connection = connection;
    }

    @Override
    public DataAction<Boolean> delete(@NotNull Class<?> type, @NotNull Object primary) throws AnnotationFormatError {
        JsonObject content = new JsonObject();
        Key key = DataUtil.getPrimary(type).getAnnotation(Key.class);
        DataUtil.addValue(content, key.name(), primary);

        return new RemoteDataActionImpl<>(connection, DataMethod.DELETE, type, content);
    }

    @Override
    public @NotNull DataAction<JsonObject> get(@NotNull Class<?> type, @NotNull Object primary) throws AnnotationFormatError {
        JsonObject content = new JsonObject();
        Key key = DataUtil.getPrimary(type).getAnnotation(Key.class);
        DataUtil.addValue(content, key.name(), primary);

        return new RemoteDataActionImpl<>(connection, DataMethod.GET, type, content);
    }

    @Override
    public @NotNull DataAction<JsonObject> put(@NotNull Class<?> type, @NotNull JsonObject data) throws AnnotationFormatError {
        JsonObject content = data.deepCopy();

        // make sure there is no primary value in the Json (The server should create it)
        Key key = DataUtil.getPrimary(type).getAnnotation(Key.class);
        content.remove(key.name());

        return new RemoteDataActionImpl<>(connection, DataMethod.PUT, type, content);
    }

    @Override
    public @NotNull DataAction<JsonObject> patch(@NotNull Class<?> type, @NotNull JsonObject data, @NotNull Object primary) throws AnnotationFormatError {
        JsonObject content = data.deepCopy();

        // make sure the primary is in the Json
        Key key = DataUtil.getPrimary(type).getAnnotation(Key.class);
        DataUtil.addValue(content, key.name(), primary);

        return new RemoteDataActionImpl<>(connection, DataMethod.PATCH, type, content);
    }
}
