package de.turtle_exception.client.internal.entities;

import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TurtlePermission;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.Group;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.requests.Action;
import de.turtle_exception.client.api.util.PermissionUtil;
import de.turtle_exception.client.internal.ActionImpl;
import de.turtle_exception.client.internal.util.TurtleSet;
import de.turtle_exception.core.net.route.Routes;
import net.dv8tion.jda.api.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

public class GroupImpl implements Group {
    private final TurtleClient client;
    private final long id;

    private String name;

    private final TurtleSet<User> users;

    private final EnumSet<TurtlePermission> permissionsTurtle;
    private final EnumSet<Permission>       permissionsDiscord;

    GroupImpl(TurtleClient client, long id, String name, TurtleSet<User> users, EnumSet<TurtlePermission> permissionsTurtle, EnumSet<Permission> permissionsDiscord) {
        this.client = client;
        this.id     = id;
        this.name   = name;

        this.users = users;

        this.permissionsTurtle  = permissionsTurtle;
        this.permissionsDiscord = permissionsDiscord;
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

    /* - PERMISSIONS / TURTLE - */

    @Override
    public boolean hasTurtlePermissionOverride(@NotNull TurtlePermission permission) {
        if (permission == TurtlePermission.UNKNOWN) return false;
        return permissionsTurtle.contains(permission);
    }

    @Override
    public @NotNull EnumSet<TurtlePermission> getTurtlePermissionOverrides() {
        return EnumSet.copyOf(permissionsTurtle);
    }

    @Override
    public @NotNull Action<Void> addTurtlePermissionOverrides(@NotNull TurtlePermission... permissions) {
        long raw = TurtlePermission.toRaw(PermissionUtil.sum(this, permissions));
        JsonObject json = new JsonObject();
        json.addProperty("permissions_turtle", raw);
        return new ActionImpl<>(client, Routes.Group.MODIFY.compile(json, this.id), null);
    }

    @Override
    public @NotNull Action<Void> removeTurtlePermissionOverrides(@NotNull TurtlePermission... permissions) {
        long raw = TurtlePermission.toRaw(PermissionUtil.subtract(this, permissions));
        JsonObject json = new JsonObject();
        json.addProperty("permissions_turtle", raw);
        return new ActionImpl<>(client, Routes.Group.MODIFY.compile(json, this.id), null);
    }

    /* - PERMISSIONS / DISCORD - */

    @Override
    public boolean hasDiscordPermissionOverride(@NotNull Permission permission) {
        if (permission == Permission.UNKNOWN) return false;
        return permissionsDiscord.contains(permission);
    }

    @Override
    public @NotNull EnumSet<Permission> getDiscordPermissionOverrides() {
        return EnumSet.copyOf(permissionsDiscord);
    }

    @Override
    public @NotNull Action<Void> addDiscordPermissionOverrides(@NotNull Permission... permissions) {
        long raw = Permission.getRaw(PermissionUtil.sum(this, permissions));
        JsonObject json = new JsonObject();
        json.addProperty("permissions_discord", raw);
        return new ActionImpl<>(client, Routes.Group.MODIFY.compile(json, this.id), null);
    }

    @Override
    public @NotNull Action<Void> removeDiscordPermissionOverrides(@NotNull Permission... permissions) {
        long raw = Permission.getRaw(PermissionUtil.subtract(this, permissions));
        JsonObject json = new JsonObject();
        json.addProperty("permissions_discord", raw);
        return new ActionImpl<>(client, Routes.Group.MODIFY.compile(json, this.id), null);
    }
}
