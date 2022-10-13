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
}
