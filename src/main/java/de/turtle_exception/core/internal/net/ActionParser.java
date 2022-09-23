package de.turtle_exception.core.internal.net;

import de.turtle_exception.core.internal.TurtleClientImpl;
import de.turtle_exception.core.internal.TurtleCore;
import de.turtle_exception.core.internal.TurtleServerImpl;
import de.turtle_exception.core.internal.net.action.AnswerableAction;
import de.turtle_exception.core.internal.net.action.RemoteActionImpl;
import de.turtle_exception.core.internal.net.server.VirtualClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// TODO: finish docs
public class ActionParser {
    /**
     * Parses a remote action from a String message.
     * <p>If the remote action is a response to the provided {@link AnswerableAction} (it should be {@code null}
     * otherwise) the local action will automatically be completed with the execution of the new
     * {@link RemoteActionImpl}.
     * @param core
     * @param msg
     * @param respondingTo
     * @return
     * @throws IllegalArgumentException
     */
    private static RemoteActionImpl parseAction(@NotNull TurtleCore core, @Nullable VirtualClient vClient, @NotNull String msg, @Nullable AnswerableAction<?> respondingTo) throws IllegalArgumentException {
        final String routeStr   = msg.substring(0, msg.indexOf("#") - 1); // -1 to remove the #
        final String contentStr = msg.substring(routeStr.length() + 1); // +1 for the #

        Route route = parseRoute(routeStr);

        // TODO: what about respondingTo?

        if (core instanceof TurtleServerImpl server) {
            if (vClient == null)
                throw new IllegalArgumentException("vClient my not be null when the provided core is of type TurtleServerImpl.");

            return new RemoteActionImpl(server, route, contentStr, vClient);
        } else if (core instanceof TurtleClientImpl client) {
            return new RemoteActionImpl(client, route, contentStr);
        }

        throw new IllegalArgumentException("Core must be either TurtleServerImpl or TurtleClientImpl.");
    }

    public static RemoteActionImpl parseAction(@NotNull TurtleServerImpl server, @NotNull VirtualClient client, @NotNull String msg, @Nullable AnswerableAction<?> respondingTo) {
        return parseAction((TurtleCore) server, client, msg, respondingTo);
    }

    public static RemoteActionImpl parseAction(@NotNull TurtleClientImpl client, @NotNull String msg, @Nullable AnswerableAction<?> respondingTo) {
        return parseAction((TurtleCore) client, null, msg, respondingTo);
    }

    private static @NotNull Route parseRoute(@NotNull String msg) throws IllegalArgumentException {
        for (Route route : Route.getRoutes())
            if (msg.equalsIgnoreCase(route.getMessage()))
                return route;
        throw new IllegalArgumentException("String does not represent a route: " + msg);
    }
}
