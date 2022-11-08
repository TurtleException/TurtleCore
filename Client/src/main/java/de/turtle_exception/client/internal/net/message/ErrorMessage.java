package de.turtle_exception.client.internal.net.message;

import com.google.gson.JsonObject;
import de.turtle_exception.client.internal.net.Connection;
import org.jetbrains.annotations.NotNull;

public class ErrorMessage extends Message {
    protected final @NotNull JsonObject error;

    public ErrorMessage(@NotNull Connection connection, long conversation, long deadline, @NotNull JsonObject error) {
        super(connection, Route.ERROR, conversation, deadline, wrapJson(error));

        this.error = error;
    }

    /* - - - */

    public @NotNull JsonObject getError() {
        return error;
    }

    /* - - - */

    public static @NotNull JsonObject wrapJson(@NotNull JsonObject error) {
        JsonObject json = new JsonObject();
        json.add("content", error.deepCopy());
        return json;
    }
}
