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
public enum Permission {
    // just a filler
    EXAMPLE((byte) 0, "Example permission"),

    /** An unknown or invalid permission. This will always be used as a default is no specific permission can be used. */
    UNKNOWN((byte) -1, "Unknown");

    private final byte   offset;
    private final String title;

    Permission(@Range(from = -1, to = 63) byte offset, @NotNull String title) {
        this.offset = offset;
        this.title  = title;
    }

    /** The offset of this permission - i.e. the bit of a long that represents this permission. */
    public byte getOffset() {
        return offset;
    }

    /** The name of this permission. */
    public @NotNull String getTitle() {
        return title;
    }

    /**
     * The mask of this permission - i.e. a long with all bits set to 0, except the one defined by
     * {@link Permission#getOffset()}.
     * */
    public long getRaw() {
        if (this == UNKNOWN) return 0;
        return 1L << offset;
    }

    /* - - - */

    /**
     * Provides a single {@link Permission} defined by the specified offset. If no permission suits that offset
     * {@link Permission#UNKNOWN} will be returned.
     * @param offset Permission bit offset.
     */
    public static @NotNull Permission fromOffset(byte offset) {
        for (Permission permission : values())
            if (permission.getOffset() == offset)
                return permission;
        return UNKNOWN;
    }

    /** Provides an {@link EnumSet} containing the permissions defined by the specified raw long. */
    public static @NotNull EnumSet<Permission> fromRaw(long raw) {
        EnumSet<Permission> permissions = EnumSet.noneOf(Permission.class);

        for (Permission p : values()) {
            if (p == UNKNOWN) continue;

            long mask = p.getOffset();
            if ((mask & raw) == raw)
                permissions.add(p);
        }

        return permissions;
    }

    /**
     * Converts one or more Permissions into a raw long. If one of the permissions is {@link Permission#UNKNOWN} it will
     * be ignored. Duplicates will be ignored and handled as if they were only provided once.
     */
    public static long toRaw(@NotNull Permission... permissions) {
        long raw = 0;

        for (Permission permission : permissions) {
            if (permission == UNKNOWN) continue;
            raw = raw | permission.getRaw();
        }

        return raw;
    }

    /**
     * Converts a {@link Collection} of Permissions into a raw long. If one of the permissions is
     * {@link Permission#UNKNOWN} it will be ignored. Duplicates will be ignored and handled as if they were only
     * provided once.
     */
    public static long toRaw(@NotNull Collection<Permission> permissions) {
        return toRaw(permissions.toArray(new Permission[0]));
    }
}
