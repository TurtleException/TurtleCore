package de.turtle_exception.client.api.requests.action;

import de.turtle_exception.client.api.entities.Group;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.requests.Action;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public interface GroupAction extends Action<Group> {
    @NotNull GroupAction setName(@NotNull String name);

    @NotNull GroupAction addUser(long id);

    default @NotNull GroupAction addUser(@NotNull User user) {
        return this.addUser(user.getId());
    }
}
