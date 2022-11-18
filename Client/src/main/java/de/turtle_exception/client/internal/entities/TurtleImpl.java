package de.turtle_exception.client.internal.entities;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.api.event.Event;
import de.turtle_exception.client.internal.util.ExceptionalConsumer;
import org.jetbrains.annotations.NotNull;

public abstract class TurtleImpl implements Turtle {
    protected final @NotNull TurtleClient client;
    protected final long id;

    protected TurtleImpl(@NotNull TurtleClient client, long id) {
        this.client = client;
        this.id = id;
    }

    @Override
    public long getId() {
        return id;
    }

    @NotNull
    @Override
    public TurtleClient getClient() {
        return client;
    }

    /* - - - */

    public abstract @NotNull TurtleImpl handleUpdate(@NotNull JsonObject json);

    protected final void apply(@NotNull JsonObject json, @NotNull String key, @NotNull ExceptionalConsumer<JsonElement> consumer) {
        try {
            JsonElement element = json.get(key);
            if (element == null) return;
            consumer.accept(element);
        } catch (Exception ignored) {
            // only apply change if the specified value is present
        }
    }

    /** Just a shortcut */
    protected final void fireEvent(@NotNull Event event) {
        this.getClient().getEventManager().handleEvent(event);
    }
}
