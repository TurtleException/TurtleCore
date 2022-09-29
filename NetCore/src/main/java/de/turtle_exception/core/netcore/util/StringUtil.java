package de.turtle_exception.core.netcore.util;

import org.jetbrains.annotations.NotNull;

public class StringUtil {
    public static @NotNull String cutEnd(@NotNull String str, int i) throws IndexOutOfBoundsException {
        return str.substring(str.length() - i);
    }
}
