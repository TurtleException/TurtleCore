package de.turtle_exception.client.api.entities;

import de.turtle_exception.client.api.entities.containers.IUserContainer;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.data.annotations.Key;
import de.turtle_exception.client.internal.data.annotations.Relation;
import de.turtle_exception.client.internal.data.annotations.Resource;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Resource(path = "groups", builder = "buildGroup")
@SuppressWarnings("unused")
public interface Group extends Turtle, IUserContainer {
    @Override
    default @NotNull Action<Group> update() {
        return this.getClient().retrieveGroup(this.getId());
    }

    /* - NAME - */

    @Key(name = "name")
    @NotNull String getName();

    @NotNull Action<Group> modifyName(@NotNull String name);

    /* - USERS - */

    @Key(name = "users", relation = Relation.MANY_TO_MANY)
    @NotNull List<User> getUsers();

    @NotNull Action<Group> addUser(long user);

    default @NotNull Action<Group> addUser(@NotNull User user) {
        return this.addUser(user.getId());
    }

    @NotNull Action<Group> removeUser(long user);

    default @NotNull Action<Group> removeUser(@NotNull User user) {
        return this.removeUser(user.getId());
    }
}
