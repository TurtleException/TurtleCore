package de.turtle_exception.client.api.event.entities.user;

import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.internal.data.annotations.Keys;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class UserUpdateNameEvent extends UserUpdateEvent<String> {
    public UserUpdateNameEvent(@NotNull User user, @NotNull String oldName, @NotNull String newName) {
        super(user, Keys.User.NAME, oldName, newName);
    }
}
