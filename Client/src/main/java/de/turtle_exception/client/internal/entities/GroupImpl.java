package de.turtle_exception.client.internal.entities;

import com.google.gson.JsonObject;
import de.turtle_exception.client.api.Permission;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.Group;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.requests.Action;
import de.turtle_exception.client.internal.ActionImpl;
import de.turtle_exception.client.internal.util.TurtleSet;
import de.turtle_exception.core.net.route.Routes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

public class GroupImpl implements Group {
    private final TurtleClient client;
    private final long id;

    private String name;

    private final TurtleSet<User> users;

    private final EnumSet<Permission> permissions;

    GroupImpl(TurtleClient client, long id, String name, TurtleSet<User> users, EnumSet<Permission> permissions) {
        this.client = client;
        this.id = id;
        this.name = name;

        this.users = users;

        this.permissions = permissions;
    }

    @Override
    public @NotNull TurtleClient getClient() {
        return this.client;
    }

    @Override
    public long getId() {
        return this.id;
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
    public @NotNull Action<Void> modifyName(@NotNull String name) {
        JsonObject json = new JsonObject();
        json.addProperty("name", name);
        return new ActionImpl<>(client, Routes.Group.MODIFY.compile(json, this.id), null);
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
    public @NotNull Action<Void> addUser(long user) {
        return new ActionImpl<>(client, Routes.Group.ADD_USER.compile(null, this.id, user), null);
    }

    @Override
    public @NotNull Action<Void> removeUser(long user) {
        return new ActionImpl<>(client, Routes.Group.DEL_USER.compile(null, this.id, user), null);
    }

    /* - PERMISSIONS - */

    @Override
    public boolean hasPermissionOverride(@NotNull Permission permission) {
        if (permission == Permission.UNKNOWN) return false;
        return permissions.contains(permission);
    }

    @Override
    public @NotNull EnumSet<Permission> getPermissionOverrides() {
        return EnumSet.copyOf(permissions);
    }
}
