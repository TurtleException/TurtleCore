package de.turtle_exception.core.internal.net;

import de.turtle_exception.core.internal.TurtleClientImpl;
import de.turtle_exception.core.internal.TurtleCore;
import de.turtle_exception.core.internal.TurtleServerImpl;
import de.turtle_exception.core.internal.net.response.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

// TODO: tidy
public class Route {
    /** Acknowledges a message or affirms a request. */
    public static final Route OK = new Route("OK", false, null, null);
    /** Denies a request with the provided reason (nullable). {@link Error} should still be used separately. */
    public static final Route NO = new Route("NO", true);

    public static class Login {
        /**
         * Sends a login request with the provided credentials (username). Once the client has sent its login
         * credentials the server should either respond with {@link Route#OK} or with an error.
         */
        public static final Route LOGIN = new Route("LOGIN", true, new LoginHandler(), null);
        /**
         * Informs the other server of the clients' current version. The server should either affirm with
         * {@link Route#OK} or send an incompatibility error. TODO: update this once error routes are implemented
         */
        public static final Route VERSION = new Route("VERSION", true, new VersionHandler(), null);
        /** Final confirmation from the server that all login steps have been completed */
        public static final Route LOGGED_IN = new Route("LOGIN OK", false, null, null);
        /**
         * Notifies the other side that the connection will now be terminated. This should silently be acknowledged by
         * the other side and the connection should be terminated on both sides. No confirmation is required.
         */
        public static final Route QUIT = new Route("QUIT", false, new QuitHandler());
    }

    public static class Error {
        // TODO
    }

    public static class Content {
        // reuse the same objects
        private static final ServerContentHandler SERVER_CONTENT_HANDLER = new ServerContentHandler();
        private static final ClientContentHandler CLIENT_CONTENT_HANDLER = new ClientContentHandler();

        public static class User {
            /** Requests all user objects */
            public static final Route GET_ALL = new Route("USER GET ALL", false, SERVER_CONTENT_HANDLER, null);
            /** Requests a single user object specified by the provided id */
            public static final Route GET = new Route("USER GET", true, SERVER_CONTENT_HANDLER, null);
            /** Deletes a user with the provided id */
            public static final Route DEL = new Route("USER DEL", true, SERVER_CONTENT_HANDLER, null);
            /** Creates or modifies a user with the provided JSON object. */
            public static final Route SET = new Route("USER SET", true, SERVER_CONTENT_HANDLER, null);
            /** Provides a user JSON that should be updated in cache */
            public static final Route UPDATE = new Route("USER UPDATE", true, null, CLIENT_CONTENT_HANDLER);
            /** Provides a JSON with multiple users that should be updated in cache */
            public static final Route UPDATE_MULTI = new Route("USER UPDATE_MULTI", true, null, CLIENT_CONTENT_HANDLER);
        }

        public static class Group {
            /** Requests all group objects */
            public static final Route GET_ALL = new Route("GROUP GET ALL", false, SERVER_CONTENT_HANDLER, null);
            /** Requests a single group object specified by the provided id */
            public static final Route GET = new Route("GROUP GET", true, SERVER_CONTENT_HANDLER, null);
            /** Deletes a group with the provided id */
            public static final Route DEL = new Route("GROUP DEL", true, SERVER_CONTENT_HANDLER, null);
            /** Creates or modifies a group with the provided JSON object. */
            public static final Route SET = new Route("GROUP SET", true, SERVER_CONTENT_HANDLER, null);
            /** Provides a group JSON that should be updated in cache */
            public static final Route UPDATE = new Route("GROUP UPDATE", true, null, CLIENT_CONTENT_HANDLER);
            /** Provides a JSON with multiple users that should be updated in cache */
            public static final Route UPDATE_MULTI = new Route("GROUP UPDATE_MULTI", true, null, CLIENT_CONTENT_HANDLER);
        }
    }

    /* --- */

    private final String message;
    private final boolean hasContent;

    /** Handles the response to a client */
    private final ResponseHandler<TurtleServerImpl> serverHandler;
    /** Handles the response ro a server */
    private final ResponseHandler<TurtleClientImpl> clientHandler;

    private Route(@NotNull String message, boolean hasContent, @Nullable ResponseHandler<TurtleServerImpl> serverHandler, @Nullable ResponseHandler<TurtleClientImpl> clientHandler) {
        this.message = message;
        this.hasContent = hasContent;

        this.serverHandler = serverHandler;
        this.clientHandler = clientHandler;
    }

    public Route(String message, boolean hasContent, @NotNull ResponseHandler<TurtleCore> handler) {
        this.message = message;
        this.hasContent = hasContent;

        this.serverHandler = handler::handle;
        this.clientHandler = handler::handle;
    }

    public @NotNull String getMessage() {
        return this.message;
    }

    public boolean hasContent() {
        return this.hasContent;
    }

    public ResponseHandler<TurtleServerImpl> getServerHandler() {
        return serverHandler;
    }

    public ResponseHandler<TurtleClientImpl> getClientHandler() {
        return clientHandler;
    }

    /* --- */

    private static final Route[] ROUTES;
    static {
        Field[] declaredFields = Route.class.getDeclaredFields();
        ArrayList<Route> routes = new ArrayList<>();

        for (Field field : declaredFields) {
            if (!Modifier.isStatic(field.getModifiers())) continue;
            if (field.getType() != Route.class)           continue;

            try {
                routes.add((Route) field.get(null));
            } catch (Throwable ignored) { }
        }

        ROUTES = routes.toArray(new Route[0]);
    }

    public static Route[] getRoutes() {
        return ROUTES;
    }
}
