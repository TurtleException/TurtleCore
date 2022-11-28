package de.turtle_exception.client.api.event.entities.user;

import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.event.entities.EntityDeleteEvent;
import org.jetbrains.annotations.NotNull;

public class UserDeleteEvent extends UserEvent implements EntityDeleteEvent<User> {
    public UserDeleteEvent(@NotNull User user) {
        super(user);
    }
}
