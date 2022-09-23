package de.turtle_exception.core.internal.net.client;

import org.jetbrains.annotations.NotNull;

record Message(int callbackCode, @NotNull String content) {
    public @NotNull Message createResponse(@NotNull String content) {
        return new Message(callbackCode(), content);
    }

    /* - - - */

    public static @NotNull String parseToServer(@NotNull Message msg)  {
        return msg.callbackCode() + "#" + msg.content();
    }

    public static @NotNull Message parseFromServer(@NotNull String msg) throws IllegalArgumentException {
        int callbackCode = parse(msg);
        String content = msg.substring(msg.indexOf(" "));

        return new Message(callbackCode, content);
    }

    private static int parse(@NotNull String str) throws IllegalArgumentException {
        try {
            String n = str.substring(0, str.indexOf(" ") - 1);
            return Integer.parseInt(n);
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            throw new IllegalArgumentException("Could not parse callback code");
        }
    }
}
