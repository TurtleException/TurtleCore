package de.turtle_exception.client.api.event.user;

import de.turtle_exception.client.api.entities.User;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class UserUpdateNameEvent extends UserUpdateEvent<String> {
    public UserUpdateNameEvent(@NotNull User user, @NotNull String oldName, @NotNull String newName) {
        super(user, "name", oldName, newName);
    }
}
