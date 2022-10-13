package de.turtle_exception.client.api.entities;

import de.turtle_exception.client.api.Permission;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

@SuppressWarnings("unused")
public interface PermissionHolder extends Turtle {
    boolean hasPermission(@NotNull Permission permission);

    @NotNull EnumSet<Permission> getPermissionOverrides();

    default long getPermissionOverridesRaw() {
        return Permission.toRaw(this.getPermissionOverrides());
    }
}
