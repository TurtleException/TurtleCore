package de.turtle_exception.core.client.internal.net.server;

import org.jetbrains.annotations.NotNull;

record Message(int callbackCode, @NotNull String content, @NotNull VirtualClient client) {
    public @NotNull Message createResponse(@NotNull String content) {
        return new Message(callbackCode(), content, client());
    }

    /* - - - */

    public static @NotNull String parseToClient(@NotNull Message msg)  {
        return msg.callbackCode() + "#" + msg.content();
    }

    public static @NotNull Message parseFromClient(@NotNull VirtualClient client, @NotNull String msg) throws IllegalArgumentException {
        int callbackCode = parse(msg);
        String content = msg.substring(msg.indexOf(" "));

        return new Message(callbackCode, content, client);
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