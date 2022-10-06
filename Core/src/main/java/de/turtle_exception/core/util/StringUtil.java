package de.turtle_exception.core.util;

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

    // note: this will not work with overlapping results
    public static int count(final @NotNull String str, final @NotNull String match) {
        int count = 0;
        int last  = 0;

        while (last != -1) {
            last = str.indexOf(match, last);

            if (last != -1) {
                count++;
                last += match.length();
            }
        }

        return count;
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

    public static @NotNull String cutEnd(@NotNull String str, int i) throws IndexOutOfBoundsException {
        return str.substring(str.length() - i);
    }
}
