package de.turtle_exception.client.internal.entities;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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

    private TurtleSet<User> users;

    GroupImpl(@NotNull TurtleClient client, long id, String name, TurtleSet<User> users) {
        super(client, id);
        this.name = name;

        this.users = users;
    }

    @Override
    public synchronized @NotNull GroupImpl handleUpdate(@NotNull JsonObject json) {
        this.apply(json, "name", element -> { this.name = element.getAsString(); });
        this.apply(json, "users", element -> {
            TurtleSet<User> set = new TurtleSet<>();
            for (JsonElement entry : element.getAsJsonArray())
                set.add(client.getUserById(entry.getAsLong()));
            this.users = set;
        });
        return this;
    }

    /* - NAME - */

    @Override
    public @NotNull String getName() {
        return this.name;
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
        return getClient().getProvider().patchEntryAdd(this, "users", user).andThenParse(Group.class);
    }

    @Override
    public @NotNull Action<Group> removeUser(long user) {
        return getClient().getProvider().patchEntryDel(this, "users", user).andThenParse(Group.class);
    }
}
