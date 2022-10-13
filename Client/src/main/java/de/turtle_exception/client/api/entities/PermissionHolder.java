package de.turtle_exception.client.api.entities;

import de.turtle_exception.client.api.Permission;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.EnumSet;

@SuppressWarnings("unused")
public interface PermissionHolder extends Turtle {
    boolean hasPermission(@NotNull Permission... permissions);

    default boolean hasPermissions(@NotNull Collection<Permission> permissions) {
        return this.hasPermission(permissions.toArray(new Permission[0]));
    }

    @NotNull EnumSet<Permission> getPermissionOverrides();

    default long getPermissionOverridesRaw() {
        return Permission.toRaw(this.getPermissionOverrides());
    }
}
