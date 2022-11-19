package de.turtle_exception.client.api.event.entities.user;

import de.turtle_exception.client.api.entities.User;
import org.jetbrains.annotations.NotNull;

public class UserCreateEvent extends UserEvent {
    public UserCreateEvent(@NotNull User user) {
        super(user);
    }
}
