package de.turtle_exception.client.api.event.user;

import de.turtle_exception.client.api.entities.User;
import org.jetbrains.annotations.NotNull;

public class UserDeleteEvent extends UserEvent {
    public UserDeleteEvent(@NotNull User user) {
        super(user);
    }
}
