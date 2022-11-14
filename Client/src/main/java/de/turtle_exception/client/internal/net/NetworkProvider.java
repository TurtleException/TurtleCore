package de.turtle_exception.client.internal.net;

import com.google.gson.JsonObject;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.Provider;
import de.turtle_exception.client.internal.data.DataUtil;
import de.turtle_exception.client.internal.data.annotations.Key;
import de.turtle_exception.client.internal.request.NetAction;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.AnnotationFormatError;

public class NetworkProvider extends Provider {
    private final Connection connection;

    public NetworkProvider(@NotNull Connection connection) {
        super(4);
        this.connection = connection;
    }

    @Override
    public <T> @NotNull Action<Boolean> delete(@NotNull Class<T> type, @NotNull Object primary) throws AnnotationFormatError {
        JsonObject content = new JsonObject();
        Key key = DataUtil.getPrimary(type).getAnnotation(Key.class);
        DataUtil.addValue(content, key.name(), primary);

        return new NetAction<>(this, connection, DataMethod.DELETE, type, content);
    }

    @Override
    public <T> @NotNull Action<JsonObject> get(@NotNull Class<T> type, @NotNull Object primary) throws AnnotationFormatError {
        JsonObject content = new JsonObject();
        Key key = DataUtil.getPrimary(type).getAnnotation(Key.class);
        DataUtil.addValue(content, key.name(), primary);

        return new NetAction<>(this, connection, DataMethod.GET, type, content);
    }

    @Override
    public <T> @NotNull Action<JsonObject> put(@NotNull Class<T> type, @NotNull JsonObject data) throws AnnotationFormatError {
        JsonObject content = data.deepCopy();

        // make sure there is no primary value in the Json (The server should create it)
        Key key = DataUtil.getPrimary(type).getAnnotation(Key.class);
        content.remove(key.name());

        return new NetAction<>(this, connection, DataMethod.PUT, type, content);
    }

    @Override
    public <T> @NotNull Action<JsonObject> patch(@NotNull Class<T> type, @NotNull JsonObject data, @NotNull Object primary) throws AnnotationFormatError {
        JsonObject content = data.deepCopy();

        // make sure the primary is in the Json
        Key key = DataUtil.getPrimary(type).getAnnotation(Key.class);
        DataUtil.addValue(content, key.name(), primary);

        return new NetAction<>(this, connection, DataMethod.PATCH, type, content);
    }
}
