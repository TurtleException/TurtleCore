package de.turtle_exception.client.api.entities;

import de.turtle_exception.client.api.entities.attribute.IUserContainer;
import de.turtle_exception.client.api.requests.Action;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings("unused")
public interface Group extends Turtle, PermissionHolder, IUserContainer {
    @NotNull String getName();

    @NotNull Action<Void> modifyName(@NotNull String name);

    @NotNull List<User> getUsers();

    @NotNull Action<Void> addUser(long user);

    default @NotNull Action<Void> addUser(@NotNull User user) {
        return this.addUser(user.getId());
    }

    @NotNull Action<Void> removeUser(long user);

    default @NotNull Action<Void> removeUser(@NotNull User user) {
        return this.removeUser(user.getId());
    }
}
