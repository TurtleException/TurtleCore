package de.turtle_exception.client.api.event.entities.user;

import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.event.entities.EntityEvent;
import org.jetbrains.annotations.NotNull;

public abstract class UserEvent extends EntityEvent<User> {
    public UserEvent(@NotNull User user) {
        super(user);
    }

    public @NotNull User getUser() {
        return this.getEntity();
    }
}
