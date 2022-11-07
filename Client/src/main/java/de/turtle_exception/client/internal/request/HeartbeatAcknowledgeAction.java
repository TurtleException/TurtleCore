package de.turtle_exception.client.internal.request;

import com.google.gson.JsonObject;
import de.turtle_exception.client.internal.net.message.Message;
import de.turtle_exception.client.internal.net.message.Route;
import org.jetbrains.annotations.NotNull;

public class HeartbeatAcknowledgeAction extends NetActionImpl<Void> {
    public HeartbeatAcknowledgeAction(@NotNull Message message) {
        super(message.getConnection(), Route.HEARTBEAT_ACK, buildJson(message));
    }

    private static @NotNull JsonObject buildJson(@NotNull Message message) {
        JsonObject json = new JsonObject();
        json.addProperty("time1", message.getJson().get("time").getAsLong());
        json.addProperty("time2", System.currentTimeMillis());
        // the rest will be filled in automatically
        return json;
    }
}
