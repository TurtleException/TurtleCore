package de.turtle_exception.client.api;

import de.turtle_exception.client.api.entities.PermissionHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Collection;
import java.util.EnumSet;

/**
 * A permission may be applied to a {@link PermissionHolder} and authorizes them to use certain parts of the application.
 * Permissions are recorded in {@code long} numbers, where each bit represents a single permission that can either be
 * allowed (1) or denied (0).
 */
public enum TurtlePermission {
    // BASICS
    CHAT(0, "Use Chats"),

    // NAMES
    NICK_CHANGE(1, "Change nickname"),
    NICK_MANAGE(2, "Manage nicknames"),

    // GROUPS & PERMISSIONS
    MANAGE_GROUPS(       3, "Manage groups"),
    MANAGE_GROUP_MEMBERS(4, "Manage group members"),
    MANAGE_PERMISSIONS(  5, "Manage permissions"),

    // MODERATION
    MOD_MUTE(       6, "Mute users"),
    MOD_KICK(       7, "Kick users"),
    MOD_BAN(        8, "Ban users"),
    MANAGE_MESSAGES(9, "Manage messages"),

    // LOGS
    LOG_VIEW(  10, "View logs"),
    LOG_MANAGE(11, "Manage logs"),

    ADMIN(63, "Administrator"),

    /** An unknown or invalid permission. This will always be used as a default is no specific permission can be used. */
    UNKNOWN(-1, "Unknown");

    private final int    offset;
    private final String title;

    TurtlePermission(@Range(from = -1, to = 63) int offset, @NotNull String title) {
        this.offset = offset;
        this.title  = title;
    }

    /** The offset of this permission - i.e. the bit of a long that represents this permission. */
    public int getOffset() {
        return offset;
    }

    /** The name of this permission. */
    public @NotNull String getTitle() {
        return title;
    }

    /**
     * The mask of this permission - i.e. a long with all bits set to 0, except the one defined by
     * {@link TurtlePermission#getOffset()}.
     * */
    public long getRaw() {
        if (this == UNKNOWN) return 0;
        return 1L << offset;
    }

    /* - - - */

    /**
     * Provides a single {@link TurtlePermission} defined by the specified offset. If no permission suits that offset
     * {@link TurtlePermission#UNKNOWN} will be returned.
     * @param offset Permission bit offset.
     */
    public static @NotNull TurtlePermission fromOffset(int offset) {
        for (TurtlePermission permission : values())
            if (permission.getOffset() == offset)
                return permission;
        return UNKNOWN;
    }

    /** Provides an {@link EnumSet} containing the permissions defined by the specified raw long. */
    public static @NotNull EnumSet<TurtlePermission> fromRaw(long raw) {
        EnumSet<TurtlePermission> permissions = EnumSet.noneOf(TurtlePermission.class);

        for (TurtlePermission p : values()) {
            if (p == UNKNOWN) continue;

            long mask = p.getOffset();
            if ((mask & raw) == raw)
                permissions.add(p);
        }

        return permissions;
    }

    /**
     * Converts one or more Permissions into a raw long. If one of the permissions is {@link TurtlePermission#UNKNOWN} it will
     * be ignored. Duplicates will be ignored and handled as if they were only provided once.
     */
    public static long toRaw(@NotNull TurtlePermission... permissions) {
        long raw = 0;

        for (TurtlePermission permission : permissions) {
            if (permission == UNKNOWN) continue;
            raw = raw | permission.getRaw();
        }

        return raw;
    }

    /**
     * Converts a {@link Collection} of Permissions into a raw long. If one of the permissions is
     * {@link TurtlePermission#UNKNOWN} it will be ignored. Duplicates will be ignored and handled as if they were only
     * provided once.
     */
    public static long toRaw(@NotNull Collection<TurtlePermission> permissions) {
        return toRaw(permissions.toArray(new TurtlePermission[0]));
    }
}
