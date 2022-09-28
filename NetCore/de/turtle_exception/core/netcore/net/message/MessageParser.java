package de.turtle_exception.core.netcore.net.message;

import de.turtle_exception.core.netcore.TurtleCore;
import de.turtle_exception.core.netcore.net.route.ContentType;
import de.turtle_exception.core.netcore.net.route.Route;
import de.turtle_exception.core.netcore.net.route.Routes;
import org.jetbrains.annotations.NotNull;

public class MessageParser {
    public static final String DELIMITER = "#";
    public static final int MESSAGE_TOKENS = 4;

    public static String escapeDelimiter(String str) {
        return str.replaceAll(DELIMITER, "\\\\" + DELIMITER);
    }

    public static String unescapeDelimiter(String str) {
        return str.replaceAll("\\\\" + DELIMITER, DELIMITER);
    }

    /* - - - */

    public static @NotNull String parse(@NotNull OutboundMessage msg) {
        return String.join(DELIMITER,
                parseIntToString(msg.getCallbackCode()),
                escapeDelimiter(msg.getCommand()),
                escapeDelimiter(msg.getRoute().getContentType().getName()),
                escapeDelimiter(msg.getCommand())
        );
    }

    public static @NotNull InboundMessage parse(@NotNull TurtleCore core, @NotNull String msg) throws IllegalArgumentException {
        String[] parts = msg.split("#");

        if (parts.length != MESSAGE_TOKENS)
            throw new IllegalArgumentException("Unexpected amount of tokens (" + parts.length + " instead of " + MESSAGE_TOKENS + ") in message: " + msg);

        int callbackCode;
        Route route = null;
        ContentType contentType = null;
        String command = unescapeDelimiter(parts[1]);
        String cTypStr = unescapeDelimiter(parts[2]);
        String content = unescapeDelimiter(parts[3]);

        try {
            callbackCode = Integer.parseUnsignedInt(parts[0]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Could not parse callbackCode.", e);
        }

        for (Route route1 : Routes.getRoutes()) {
            if (command.equals(route1.getCommand())) {
                route = route1;
                break;
            }
        }
        if (route == null)
            throw new IllegalArgumentException("Unknown command: " + command);

        for (ContentType value : ContentType.values()) {
            if (value.getName().equals(cTypStr)) {
                contentType = value;
                break;
            }
        }
        if (contentType == null)
            throw new IllegalArgumentException("Unknown ContentType: " + cTypStr);

        // TODO: include timeout in string message (?)
        return new InboundMessage(core, callbackCode, route, content, core.getRouteManager().getRouteFinalizer(route));
    }

    /* - - - */

    private static String parseIntToString(int i) {
        return String.format("%32s", Integer.toBinaryString(i)).replace(' ', '0');
    }
}
