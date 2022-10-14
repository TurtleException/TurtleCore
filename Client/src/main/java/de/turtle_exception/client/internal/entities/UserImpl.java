package de.turtle_exception.client.internal.entities;

import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TurtlePermission;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.Group;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.requests.Action;
import de.turtle_exception.client.internal.ActionImpl;
import de.turtle_exception.core.net.route.Routes;
import net.dv8tion.jda.api.Permission;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

public class UserImpl implements User {
    private final TurtleClient client;
    private final long id;

    private String name;

    private final ArrayList<Long> discord;
    private final ArrayList<UUID> minecraft;

    private final EnumSet<TurtlePermission> permissionOverridesTurtle;
    private final EnumSet<Permission>       permissionOverridesDiscord;

    UserImpl(TurtleClient client, long id, String name, ArrayList<Long> discord, ArrayList<UUID> minecraft, EnumSet<TurtlePermission> permissionOverridesTurtle, EnumSet<Permission> permissionOverridesDiscord) {
        this.client = client;
        this.id     = id;
        this.name   = name;

        this.discord   = discord;
        this.minecraft = minecraft;

        this.permissionOverridesTurtle  = permissionOverridesTurtle;
        this.permissionOverridesDiscord = permissionOverridesDiscord;
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

    // TODO: use this (route finalizer)
    public void setName(@NotNull String name) {
        this.name = name;
    }

    @Override
    public @NotNull Action<Void> modifyName(@NotNull String name) {
        JsonObject json = new JsonObject();
        json.addProperty("name", name);
        return new ActionImpl<>(client, Routes.User.MODIFY.compile(json, this.id), null);
    }

    /* - DISCORD - */

    @Override
    public @NotNull List<Long> getDiscordIds() {
        return List.copyOf(discord);
    }

    @Override
    public @NotNull Action<Void> addDiscordId(long discordId) {
        return new ActionImpl<>(client, Routes.User.ADD_DISCORD.compile(null, this.id, discordId), null);
    }

    @Override
    public @NotNull Action<Void> removeDiscordId(long discordId) {
        return new ActionImpl<>(client, Routes.User.DEL_DISCORD.compile(null, this.id, discordId), null);
    }

    /* - MINECRAFT - */

    @Override
    public @NotNull List<UUID> getMinecraftIds() {
        return List.copyOf(minecraft);
    }

    @Override
    public @NotNull Action<Void> addMinecraftId(@NotNull UUID minecraftId) {
        return new ActionImpl<>(client, Routes.User.ADD_MINECRAFT.compile(null, this.id, minecraftId), null);
    }

    @Override
    public @NotNull Action<Void> removeMinecraftId(@NotNull UUID minecraftId) {
        return new ActionImpl<>(client, Routes.User.DEL_MINECRAFT.compile(null, this.id, minecraftId), null);
    }

    /* - PERMISSIONS - */

    @Override
    public boolean hasTurtlePermission(@NotNull TurtlePermission permission) {
        if (this.hasTurtlePermissionOverride(permission)) return true;
        for (Group group : getGroups())
            if (group.hasTurtlePermission(permission)) return true;
        return false;
    }

    @Override
    public boolean hasTurtlePermissionOverride(@NotNull TurtlePermission permission) {
        if (permission == TurtlePermission.UNKNOWN) return false;
        return permissionOverridesTurtle.contains(permission);
    }

    @Override
    public @NotNull EnumSet<TurtlePermission> getTurtlePermissions() {
        EnumSet<TurtlePermission> permissions = EnumSet.copyOf(permissionOverridesTurtle);
        for (Group group : getGroups())
            permissions.addAll(group.getTurtlePermissions());
        return permissions;
    }

    @Override
    public @NotNull EnumSet<TurtlePermission> getTurtlePermissionOverrides() {
        return EnumSet.copyOf(permissionOverridesTurtle);
    }

    /* - PERMISSIONS / DISCORD - */

    @Override
    public boolean hasDiscordPermission(@NotNull Permission permission) {
        if (this.hasDiscordPermissionOverride(permission)) return true;
        for (Group group : getGroups())
            if (group.hasDiscordPermission(permission)) return true;
        return false;
    }

    @Override
    public boolean hasDiscordPermissionOverride(@NotNull Permission permission) {
        if (permission == Permission.UNKNOWN) return false;
        return permissionOverridesDiscord.contains(permission);
    }

    @Override
    public @NotNull EnumSet<Permission> getDiscordPermissions() {
        EnumSet<Permission> permissions = EnumSet.copyOf(permissionOverridesDiscord);
        for (Group group : getGroups())
            permissions.addAll(group.getDiscordPermissions());
        return permissions;
    }

    @Override
    public @NotNull EnumSet<Permission> getDiscordPermissionOverrides() {
        return EnumSet.copyOf(permissionOverridesDiscord);
    }
}
