package de.turtle_exception.client.api.entities;

import de.turtle_exception.client.api.Permission;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

@SuppressWarnings("unused")
public interface PermissionHolder extends Turtle {
    default boolean hasPermission(@NotNull Permission permission) {
        return this.hasPermissionOverride(permission);
    }

    boolean hasPermissionOverride(@NotNull Permission permission);

    default @NotNull EnumSet<Permission> getPermissions() {
        return this.getPermissionOverrides();
    }

    @NotNull EnumSet<Permission> getPermissionOverrides();

    default long getPermissionOverridesRaw() {
        return Permission.toRaw(this.getPermissionOverrides());
    }

    /* - DISCORD - */

    default boolean hasDiscordPermission(@NotNull net.dv8tion.jda.api.Permission permission) {
        return this.hasDiscordPermissionOverride(permission);
    }

    boolean hasDiscordPermissionOverride(@NotNull net.dv8tion.jda.api.Permission permission);

    default @NotNull EnumSet<net.dv8tion.jda.api.Permission> getDiscordPermissions() {
        return this.getDiscordPermissionOverrides();
    }

    @NotNull EnumSet<net.dv8tion.jda.api.Permission> getDiscordPermissionOverrides();

    default long getDiscordPermissionOverridesRaw() {
        return net.dv8tion.jda.api.Permission.getRaw(this.getDiscordPermissionOverrides());
    }
}
