package de.turtle_exception.client.internal.request;

import com.google.gson.JsonObject;
import de.turtle_exception.client.internal.net.Connection;
import de.turtle_exception.client.internal.net.message.Message;
import de.turtle_exception.client.internal.net.message.Route;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class HeartbeatAction extends NetActionImpl<Long> {
    public HeartbeatAction(@NotNull Connection connection) {
        super(connection, Route.HEARTBEAT, buildJson(), HeartbeatAction::getPing);

        this.successFinalizer = ping -> {
            connection.getLogger().log(Level.FINER, "Heartbeat! Ping: " + ping);
        };
        this.failureFinalizer = throwable -> {
            connection.getLogger().log(Level.WARNING, "Heartbeat timed out. Closing connection...");
            connection.stop(true);
        };
    }

    private static long getPing(@NotNull Message message) {
        final long time3 = System.currentTimeMillis();
        final long time1 = message.getJson().get("time1").getAsLong();
        return time3 - time1;
    }

    private static @NotNull JsonObject buildJson() {
        JsonObject json = new JsonObject();
        json.addProperty("time", System.currentTimeMillis());
        // the rest will be filled in automatically
        return json;
    }
}
