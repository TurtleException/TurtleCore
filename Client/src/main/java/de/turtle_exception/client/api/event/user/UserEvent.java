package de.turtle_exception.client.api.event.user;

import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.event.Event;
import org.jetbrains.annotations.NotNull;

public abstract class UserEvent extends Event {
    protected final User user;

    public UserEvent(@NotNull User user) {
        super(user.getClient());
        this.user = user;
    }

    public @NotNull User getUser() {
        return user;
    }
}
