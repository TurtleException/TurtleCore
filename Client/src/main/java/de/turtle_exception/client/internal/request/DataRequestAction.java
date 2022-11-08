package de.turtle_exception.client.internal.request;

import com.google.gson.JsonObject;
import de.turtle_exception.client.internal.Provider;
import de.turtle_exception.client.internal.data.DataUtil;
import de.turtle_exception.client.internal.net.message.*;
import de.turtle_exception.client.internal.util.Checks;
import de.turtle_exception.client.internal.util.JsonUtil;
import kotlin.NotImplementedError;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Represents an incoming data request. */
public class DataRequestAction extends NetActionImpl<Void> {
    protected final @NotNull Message request;

    public DataRequestAction(@NotNull Message message) throws IllegalArgumentException {
        super(message.getConnection(), Route.DATA, new JsonObject());

        if (message.getRoute() != Route.DATA)
            throw new IllegalArgumentException("Message must be of route type DATA");

        this.request = message;
        this.conversation = request.getConversation();
    }

    @Override
    public @NotNull Message buildMessage() throws Exception {
        try {
            return this.doBuildMessage();
        } catch (Throwable e) {
            this.json = new JsonObject();
            this.route = Route.ERROR;
            this.terminating = true;

            return new ErrorMessage(connection, conversation, deadline, JsonUtil.buildError(e, "Internal error."));
        }
    }

    public @NotNull Message doBuildMessage() throws Throwable {
        JsonObject requestJson = request.getJson().getAsJsonObject("data");

        Class<?>     type = Class.forName(requestJson.get("type").getAsString());
        Object    primary = DataUtil.getPrimaryValue(requestJson, type);
        DataMethod method = DataMethod.of(requestJson.get("method").getAsString());
        Provider provider = this.getClient().getProvider();

        if (method == null)
            throw new NotImplementedError("Unknown data method: " + requestJson.get("method").getAsString());

        if (Checks.equalsAny(method, DataMethod.UPDATE, DataMethod.REMOVE)) {
            JsonObject error = switch (method) {
                case UPDATE -> cacheUpdate(type, requestJson);
                case REMOVE -> cacheRemove(type, primary);
                default -> throw new AssertionError("Unexpected value: " + method);
            };

            if (error == null)
                return new Message(connection, Route.OK, conversation, deadline, new JsonObject());
            else
                return new ErrorMessage(connection, conversation, deadline, error);
        }

        if (method == DataMethod.DELETE) {
            if (provider.delete(type, primary).complete())
                return new Message(connection, Route.OK, conversation, deadline, new JsonObject());
            else
                return new ErrorMessage(connection, conversation, deadline, JsonUtil.buildError("Could not delete resource"));
        } else if (method == DataMethod.GET) {
            JsonObject obj = provider.get(type, primary).complete();
            return new DataMessage(connection, conversation, deadline, DataMethod.UPDATE, type, obj);
        } else if (method == DataMethod.PUT) {
            JsonObject obj = provider.put(type, (JsonObject) requestJson.get("content")).complete();
            return new DataMessage(connection, conversation, deadline, DataMethod.UPDATE, type, obj);
        } else if (method == DataMethod.PATCH) {
            JsonObject obj = provider.patch(type, (JsonObject) requestJson.get("content"), primary).complete();
            return new DataMessage(connection, conversation, deadline, DataMethod.UPDATE, type, obj);
        }

        throw new NotImplementedError("Unknown data method: " + method.name());
    }

    protected @Nullable JsonObject cacheUpdate(@NotNull Class<?> type, @NotNull JsonObject data) {
        try {
            getClient().updateCache(type, data);

            // no error
            return null;
        } catch (IllegalArgumentException e) {
            return JsonUtil.buildError(e, "Illegal cache operation");
        }
    }

    protected @Nullable JsonObject cacheRemove(@NotNull Class<?> type, @NotNull Object primary) {
        try {
            getClient().removeCache(type, primary);

            // no error
            return null;
        } catch (IllegalArgumentException | ClassCastException e) {
            return JsonUtil.buildError(e, "Illegal cache operation");
        }
    }
}
