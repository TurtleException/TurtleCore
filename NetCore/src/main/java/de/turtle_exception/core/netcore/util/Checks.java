package de.turtle_exception.core.netcore.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A collection of basic checks that would otherwise repeat unnecessarily.
 */
public class Checks {
    /**
     * Checks whether one or more objects is {@code null}. This method will only return {@code true} if none of the
     * provided objects is {@code null}.
     * @param obj An initial object
     * @param other More (optional) objects
     * @return {@code true} if, and only if, none of the provided objects is {@code null}.
     */
    public static boolean nonNulls(Object obj, Object... other) {
        if (obj == null) return false;
        for (Object o : other)
            if (o == null) return false;
        return true;
    }

    /**
     * Checks whether an object is {@code null} and if so, throws a {@link NullPointerException} with the message
     * "{@code X may not be null}" ({@code X} being the second parameter, this should be some sort of identifier for the
     * provided object).
     */
    public static void nonNull(Object obj, @NotNull String name) throws NullPointerException {
        if (obj == null)
            throw new NullPointerException(name + " may not be null");
    }

    /**
     * Returns the first parameter if it is not {@code null}, otherwise provides the second parameter.
     */
    public static <T> @NotNull T nullOr(@Nullable T o1, @NotNull T o2) {
        return o1 != null ? o1 : o2;
    }
}
