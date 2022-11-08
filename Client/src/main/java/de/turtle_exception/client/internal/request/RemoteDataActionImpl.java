package de.turtle_exception.client.internal.request;

import com.google.gson.JsonObject;
import de.turtle_exception.client.api.request.NetAction;
import de.turtle_exception.client.internal.net.Connection;
import de.turtle_exception.client.internal.net.message.DataMessage;
import de.turtle_exception.client.internal.net.message.DataMethod;
import de.turtle_exception.client.internal.net.message.Message;
import de.turtle_exception.client.internal.net.message.Route;
import de.turtle_exception.client.internal.util.time.TurtleUtil;
import org.jetbrains.annotations.NotNull;

public class RemoteDataActionImpl<T> extends DataActionImpl<T> implements NetAction<T> {
    protected final @NotNull Connection connection;
    protected final long timeout;

    public RemoteDataActionImpl(@NotNull Connection connection, @NotNull DataMethod method, @NotNull Class<T> type, @NotNull JsonObject content, long timeout) {
        super(connection.getAdapter().getClient(), method, type, content);
        this.connection = connection;
        this.timeout = timeout;
    }

    @Override
    public @NotNull Message buildMessage() {
        return new DataMessage(connection, TurtleUtil.newId(0), System.currentTimeMillis() + timeout, method, type, content);
    }

    @Override
    public @NotNull Connection getConnection() {
        return connection;
    }

    @Override
    public @NotNull Route getRoute() {
        return Route.DATA;
    }

    @Override
    public @NotNull JsonObject getJson() {
        return DataMessage.wrapJson(method, type, content);
    }

    /* - - - */

    @Override
    public @NotNull T createResult(@NotNull Message response) throws Exception {
        return getClient().getJsonBuilder().buildObject(type, response.getJson().getAsJsonObject("content"));
    }
}
