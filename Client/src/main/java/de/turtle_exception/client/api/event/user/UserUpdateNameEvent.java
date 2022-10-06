package de.turtle_exception.client.api.event.user;

import de.turtle_exception.client.api.entities.User;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class UserUpdateNameEvent extends UserEvent {
    private final String oldName;
    private final String newName;

    public UserUpdateNameEvent(@NotNull User user, @NotNull String oldName, @NotNull String newName) {
        super(user);

        this.oldName = oldName;
        this.newName = newName;
    }

    public @NotNull String getOldName() {
        return oldName;
    }

    public @NotNull String getNewName() {
        return newName;
    }
}
