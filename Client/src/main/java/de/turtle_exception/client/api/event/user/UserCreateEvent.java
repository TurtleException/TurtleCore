package de.turtle_exception.client.api.event.user;

import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.User;
import org.jetbrains.annotations.NotNull;

public class UserCreateEvent extends UserEvent {
    public UserCreateEvent(@NotNull TurtleClient client, @NotNull User user) {
        super(client, user);
    }
}
