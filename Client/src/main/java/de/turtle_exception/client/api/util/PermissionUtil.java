package de.turtle_exception.client.api.util;

import de.turtle_exception.client.api.TurtlePermission;
import de.turtle_exception.client.api.entities.PermissionHolder;
import net.dv8tion.jda.api.Permission;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class PermissionUtil {
    /* - TURTLE - */

    public static @NotNull EnumSet<TurtlePermission> sum(@NotNull PermissionHolder holder, @NotNull TurtlePermission... permissions) {
        EnumSet<TurtlePermission> permissionSet = holder.getTurtlePermissionOverrides();
        for (TurtlePermission permission : permissions) {
            if (permission == TurtlePermission.UNKNOWN) continue;
            permissionSet.add(permission);
        }
        return permissionSet;
    }

    public static @NotNull EnumSet<TurtlePermission> subtract(@NotNull PermissionHolder holder, @NotNull TurtlePermission... permissions) {
        EnumSet<TurtlePermission> permissionSet = holder.getTurtlePermissionOverrides();
        for (TurtlePermission permission : permissions)
            permissionSet.remove(permission);
        return permissionSet;
    }

    /* - DISCORD - */

    public static @NotNull EnumSet<Permission> sum(@NotNull PermissionHolder holder, @NotNull Permission... permissions) {
        EnumSet<Permission> permissionSet = holder.getDiscordPermissionOverrides();
        for (Permission permission : permissions) {
            if (permission == Permission.UNKNOWN) continue;
            permissionSet.add(permission);
        }
        return permissionSet;
    }

    public static @NotNull EnumSet<Permission> subtract(@NotNull PermissionHolder holder, @NotNull Permission... permissions) {
        EnumSet<Permission> permissionSet = holder.getDiscordPermissionOverrides();
        for (Permission permission : permissions)
            permissionSet.remove(permission);
        return permissionSet;
    }
}
