package de.turtle_exception.core.util;

public class StringUtil {
    private StringUtil() { }

    public static String join(String delimiter, int[] arr) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            builder.append(arr[i]);
            if (i < arr.length - 1)
                builder.append(delimiter);
        }
        return builder.toString();
    }
}
