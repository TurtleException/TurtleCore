package de.turtle_exception.client.internal.net.message;

import com.google.gson.JsonObject;
import de.turtle_exception.client.internal.TurtleClientImpl;
import de.turtle_exception.client.internal.net.Connection;
import org.jetbrains.annotations.NotNull;

public class DataMessage extends Message {
    protected final @NotNull DataMethod method;
    protected final @NotNull Class<?> contentType;

    public DataMessage(@NotNull Connection connection, long conversation, long deadline, @NotNull DataMethod method, @NotNull Class<?> contentType, @NotNull JsonObject content) {
        super(connection, Route.DATA, conversation, deadline, wrapJson(method, contentType, content));

        this.method = method;
        this.contentType = contentType;
    }

    public DataMessage(@NotNull Connection connection, long conversation, long deadline, @NotNull DataMethod method, @NotNull Object object) {
        this(connection, conversation, deadline, method, object.getClass(), ((TurtleClientImpl) connection.getAdapter().getClient()).getJsonBuilder().buildJson(object));
    }

    public DataMessage(@NotNull Connection connection, long conversation, @NotNull DataMethod method, @NotNull Object object) {
        this(connection, conversation, System.currentTimeMillis() + connection.getAdapter().getClient().getDefaultTimeoutOutbound(), method, object);
    }

    /* - - - */

    public @NotNull DataMethod getMethod() {
        return method;
    }

    public @NotNull Class<?> getContentType() {
        return contentType;
    }

    /* - - - */

    public static @NotNull JsonObject wrapJson(@NotNull DataMethod method, @NotNull Class<?> type, @NotNull JsonObject content) {
        JsonObject json = new JsonObject();
        json.addProperty("method", method.code);
        json.addProperty("content-type", type.getSimpleName());
        json.add("content", content.deepCopy());
        return json;
    }
}
