package de.turtle_exception.client.api.event.user;

import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.event.Event;
import org.jetbrains.annotations.NotNull;

public abstract class UserEvent extends Event {
    protected final User user;

    public UserEvent(@NotNull TurtleClient client, @NotNull User user) {
        super(client);
        this.user = user;
    }

    public @NotNull User getUser() {
        return user;
    }
}
