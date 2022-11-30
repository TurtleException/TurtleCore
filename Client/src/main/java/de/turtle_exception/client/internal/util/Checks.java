package de.turtle_exception.client.internal.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Checks {
    private Checks() { }

    public static boolean nonNulls(Object obj, Object... other) {
        if (obj == null) return false;
        for (Object o : other)
            if (o == null) return false;
        return true;
    }

    public static void nonNull(Object obj, @NotNull String name) throws NullPointerException {
        if (obj == null)
            throw new NullPointerException(name + " may not be null");
    }

    public static void nonNull(Object obj) throws NullPointerException {
        if (obj == null)
            throw new NullPointerException("Object may not be null");
    }

    public static <T> @NotNull T nullOr(@Nullable T o1, @NotNull T o2) {
        return o1 != null ? o1 : o2;
    }

    public static boolean equalsNeither(@NotNull Object obj, @NotNull Object... other) {
        for (Object o : other)
            if (obj.equals(o)) return false;
        return true;
    }

    public static boolean equalsAny(@NotNull Object obj, @NotNull Object... other) {
        for (Object o : other)
            if (obj.equals(o)) return true;
        return false;
    }
}
