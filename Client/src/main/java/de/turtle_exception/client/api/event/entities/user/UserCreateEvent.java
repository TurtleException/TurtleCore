package de.turtle_exception.client.api.event.entities.user;

import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.event.entities.EntityCreateEvent;
import org.jetbrains.annotations.NotNull;

public class UserCreateEvent extends UserEvent implements EntityCreateEvent<User> {
    public UserCreateEvent(@NotNull User user) {
        super(user);
    }
}
