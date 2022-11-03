package de.turtle_exception.client.api.entities;

import de.turtle_exception.client.api.entities.attribute.IUserContainer;
import de.turtle_exception.client.api.requests.Action;
import de.turtle_exception.client.internal.data.annotations.Key;
import de.turtle_exception.client.internal.data.annotations.Relation;
import de.turtle_exception.client.internal.data.annotations.Resource;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Resource(name = "groups", builder = "buildGroup")
@SuppressWarnings("unused")
public interface Group extends Turtle, IUserContainer {
    @Key(name = "name")
    @NotNull String getName();

    @NotNull Action<Void> modifyName(@NotNull String name);

    @Key(name = "group_users", relation = Relation.MANY_TO_MANY)
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
