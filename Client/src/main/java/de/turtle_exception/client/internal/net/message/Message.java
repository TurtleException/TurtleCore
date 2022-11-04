package de.turtle_exception.client.internal.net.message;

import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.internal.net.Connection;
import de.turtle_exception.client.internal.util.Checks;
import org.jetbrains.annotations.NotNull;

public class Message {
    protected final @NotNull Connection connection;

    protected final @NotNull Route route;
    protected final long conversation;
    protected final long deadline;
    protected final @NotNull JsonObject json;

    public boolean done      = false;
    public boolean cancelled = false;

    public Message(@NotNull Connection connection, @NotNull Route route, long conversation, long deadline, @NotNull JsonObject json) {
        this.connection = connection;

        this.route        = route;
        this.deadline     = deadline;
        this.conversation = conversation;
        this.json         = json;
    }

    public @NotNull JsonObject toJson() {
        JsonObject json = this.json.deepCopy();

        json.addProperty("route", route.code);
        json.addProperty("conversation", conversation);
        json.addProperty("deadline", deadline);

        return json;
    }

    public static @NotNull Message ofJson(@NotNull Connection connection, JsonObject json) throws IllegalArgumentException {
        try {
            Checks.nonNull(json, "JSON");

            Route route = Route.of(json.get("route").getAsInt());
            long conversation = json.get("conversation").getAsLong();
            long deadline     = json.get("deadline").getAsLong();

            Checks.nonNull(route, "Route property");

            JsonObject newJson = json.deepCopy();
            newJson.remove("route");
            newJson.remove("conversation");
            newJson.remove("deadline");

            return new Message(connection, route, conversation, deadline, newJson);
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not parse Message.", e);
        }
    }

    /* - - - */

    public @NotNull TurtleClient getClient() {
        return connection.getAdapter().getClient();
    }

    public @NotNull Connection getConnection() {
        return connection;
    }

    public @NotNull Route getRoute() {
        return route;
    }

    public long getConversation() {
        return conversation;
    }

    public long getDeadline() {
        return deadline;
    }

    public @NotNull JsonObject getJson() {
        return json;
    }
}