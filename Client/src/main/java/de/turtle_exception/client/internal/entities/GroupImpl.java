package de.turtle_exception.client.internal.entities;

import com.google.gson.JsonArray;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.Group;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.util.TurtleSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GroupImpl extends TurtleImpl implements Group {
    private String name;

    private final TurtleSet<User> users;

    GroupImpl(@NotNull TurtleClient client, long id, String name, TurtleSet<User> users) {
        super(client, id);
        this.name = name;

        this.users = users;
    }

    @Override
    public @NotNull Action<Group> update() {
        return getClient().getProvider().get(this.getClass(), getId()).andThenParse(Group.class);
    }

    /* - NAME - */

    @Override
    public @NotNull String getName() {
        return this.name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    @Override
    public @NotNull Action<Group> modifyName(@NotNull String name) {
        return getClient().getProvider().patch(this, "name", name).andThenParse(Group.class);
    }

    /* - MEMBERS - */

    @Override
    public @NotNull List<User> getUsers() {
        return List.copyOf(users);
    }

    public @NotNull TurtleSet<User> getUserSet() {
        return users;
    }

    @Override
    public @Nullable User getUserById(long id) {
        return users.get(id);
    }

    @Override
    public @NotNull Action<Group> addUser(long user) {
        JsonArray arr = new JsonArray();
        for (User aUser : users)
            arr.add(aUser.getId());
        arr.add(user);
        return getClient().getProvider().patch(this, "users", arr).andThenParse(Group.class);
    }

    @Override
    public @NotNull Action<Group> removeUser(long user) {
        JsonArray arr = new JsonArray();
        for (User aUser : users)
            if (aUser.getId() != user)
                arr.add(aUser.getId());
        return getClient().getProvider().patch(this, "users", arr).andThenParse(Group.class);
    }
}
