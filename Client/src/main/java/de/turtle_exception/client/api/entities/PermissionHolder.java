package de.turtle_exception.client.api.entities;

import de.turtle_exception.client.api.TurtlePermission;
import net.dv8tion.jda.api.Permission;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

@SuppressWarnings("unused")
public interface PermissionHolder extends Turtle {
    default boolean hasTurtlePermission(@NotNull TurtlePermission permission) {
        return this.hasTurtlePermissionOverride(permission);
    }

    boolean hasTurtlePermissionOverride(@NotNull TurtlePermission permission);

    default @NotNull EnumSet<TurtlePermission> getTurtlePermissions() {
        return this.getTurtlePermissionOverrides();
    }

    @NotNull EnumSet<TurtlePermission> getTurtlePermissionOverrides();

    default long getTurtlePermissionOverridesRaw() {
        return TurtlePermission.toRaw(this.getTurtlePermissionOverrides());
    }

    /* - DISCORD - */

    default boolean hasDiscordPermission(@NotNull Permission permission) {
        return this.hasDiscordPermissionOverride(permission);
    }

    boolean hasDiscordPermissionOverride(@NotNull Permission permission);

    default @NotNull EnumSet<Permission> getDiscordPermissions() {
        return this.getDiscordPermissionOverrides();
    }

    @NotNull EnumSet<Permission> getDiscordPermissionOverrides();

    default long getDiscordPermissionOverridesRaw() {
        return Permission.getRaw(this.getDiscordPermissionOverrides());
    }
}