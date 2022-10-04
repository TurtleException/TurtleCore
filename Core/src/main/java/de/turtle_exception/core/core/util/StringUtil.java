package de.turtle_exception.core.core.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class StringUtil {
    public static String join(String delimiter, int[] arr) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            builder.append(arr[i]);
            if (i < arr.length - 1)
                builder.append(".");
        }
        return builder.toString();
    }

    public static @Nullable String getToken(@NotNull String str, @NotNull String regex, int index) {
        String[] arr = str.split(regex);
        if (arr.length < index)
            return null;
        return arr[index];
    }

    public static @Nullable Long parseLong(@Nullable String str) {
        if (str == null) return null;
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static @Nullable UUID parseUUID(@Nullable String str) {
        if (str == null) return null;
        try {
            return UUID.fromString(str);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
