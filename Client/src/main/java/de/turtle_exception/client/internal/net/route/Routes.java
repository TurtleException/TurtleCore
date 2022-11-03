package de.turtle_exception.client.internal.net.route;

import de.turtle_exception.client.internal.net.message.InboundMessage;
import de.turtle_exception.client.internal.net.message.OutboundMessage;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

/**
 * A collection of prepared {@link Route} objects that can be used as templates when creating {@link OutboundMessage}
 * objects or as an anchor to provide information in {@link InboundMessage} objects.
 */
public class Routes {
    private Routes() { }

    /** Acknowledges a request as successful. <p><b>Direction: </b> Client <-> Server */
    public static final Route OK    = new Route(Method.RESPOND, "ok"   , false);
    /** Informs that a request failed and provides information on why. <p><b>Direction: </b> Client <-> Server */
    public static final Route ERROR = new Route(Method.ERROR  , "error", true );
    /** Informs that the connection will now be closed. <p><b>Direction: </b> Client <-> Server */
    public static final Route QUIT  = new Route(Method.META   , "quit" , false);

    /**
     * An empty route that can be used to respond to a request.
     * <p><b>Direction: </b> Client <-> Server
     */
    public static final Route RESPONSE = new Route(Method.RESPOND, "", true);

    public static class Group {
        /** Requests a single group. <p><b>Direction: </b> Client -> Server */
        public static final Route GET     = new Route(Method.GET   , "groups/{group.id}", false);
        /** Requests all a available groups. <p><b>Direction: </b> Client -> Server */
        public static final Route GET_ALL = new Route(Method.GET   , "groups"           , false);
        /** Completely deletes a group. <p><b>Direction: </b> Client -> Server */
        public static final Route DEL     = new Route(Method.DELETE, "groups/{group.id}", false);
        /** Creates a new group. <p><b>Direction: </b> Client -> Server */
        public static final Route CREATE  = new Route(Method.PUT   , "groups"           , true );
        /** Partially modifies a group. <p><b>Direction: </b> Client -> Server */
        public static final Route MODIFY  = new Route(Method.PATCH , "groups/{group.id}", true );

        /** Provides a group that has been updated. <p><b>Direction: </b> Client <- Server */
        public static final Route UPDATE = new Route(Method.UPDATE, "groups/{group.id}", true );
        /** Notifies that the specified group has been deleted. <p><b>Direction: </b> Client <- Server */
        public static final Route REMOVE = new Route(Method.REMOVE, "groups/{group.id}", false);

        /** Adds a user to a group. <p><b>Direction: </b> Client -> Server */
        public static final Route ADD_USER = new Route(Method.PUT   , "groups/{group}/members/{user}", false);
        /** Removes a user from a group. <p><b>Direction: </b> Client -> Server */
        public static final Route DEL_USER = new Route(Method.DELETE, "groups/{group}/members/{user}", false);
    }

    public static class User {
        /** Requests a single user. <p><b>Direction: </b> Client -> Server */
        public static final Route GET     = new Route(Method.GET   , "users/{user.id}", false);
        /** Requests all a available users. <p><b>Direction: </b> Client -> Server */
        public static final Route GET_ALL = new Route(Method.GET   , "users"          , false);
        /** Completely deletes a user. <p><b>Direction: </b> Client -> Server */
        public static final Route DEL     = new Route(Method.DELETE, "users/{user.id}", false);
        /** Creates a new user. <p><b>Direction: </b> Client -> Server */
        public static final Route CREATE  = new Route(Method.PUT   , "users"          , true );
        /** Partially modifies a user. <p><b>Direction: </b> Client -> Server */
        public static final Route MODIFY  = new Route(Method.PATCH , "users/{user.id}", true );

        /** Provides a user that has been updated. <p><b>Direction: </b> Client <- Server */
        public static final Route UPDATE = new Route(Method.UPDATE, "users/{user.id}", true );
        /** Notifies that the specified user has been deleted. <p><b>Direction: </b> Client <- Server */
        public static final Route REMOVE = new Route(Method.REMOVE, "users/{user.id}", false);

        /** Adds a Discord account to a user. <p><b>Direction: </b> Client -> Server */
        public static final Route ADD_DISCORD = new Route(Method.PUT   , "users/{user.id}/discord/{discord.id}", false);
        /** Removes a Discord account from a user. <p><b>Direction: </b> Client -> Server */
        public static final Route DEL_DISCORD = new Route(Method.DELETE, "users/{user.id}/discord/{discord.id}", false);

        /** Adds a Minecraft account to a user. <p><b>Direction: </b> Client -> Server */
        public static final Route ADD_MINECRAFT = new Route(Method.PUT   , "users/{user.id}/minecraft/{minecraft.id}", false);
        /** Removes a Minecraft account from a user. <p><b>Direction: </b> Client -> Server */
        public static final Route DEL_MINECRAFT = new Route(Method.DELETE, "users/{user.id}/minecraft/{minecraft.id}", false);
    }

    public static class Ticket {
        /** Requests a single ticket. <p><b>Direction: </b> Client -> Server */
        public static final Route GET     = new Route(Method.GET   , "tickets/{ticket.id}", false);
        /** Requests all a available ticket. <p><b>Direction: </b> Client -> Server */
        public static final Route GET_ALL = new Route(Method.GET   , "tickets"            , false);
        /** Completely deletes a ticket. <p><b>Direction: </b> Client -> Server */
        public static final Route DEL     = new Route(Method.DELETE, "tickets/{ticket.id}", false);
        /** Creates a new ticket. <p><b>Direction: </b> Client -> Server */
        public static final Route CREATE  = new Route(Method.PUT   , "tickets"            , true );
        /** Partially modifies a ticket. <p><b>Direction: </b> Client -> Server */
        public static final Route MODIFY  = new Route(Method.PATCH , "tickets/{ticket.id}", true );

        /** Provides a ticket that has been updated. <p><b>Direction: </b> Client <- Server */
        public static final Route UPDATE = new Route(Method.UPDATE, "tickets/{ticket.id}", true );
        /** Notifies that the specified ticket has been deleted. <p><b>Direction: </b> Client <- Server */
        public static final Route REMOVE = new Route(Method.REMOVE, "tickets/{ticket.id}", false);

        /** Adds a Discord account to a user. <p><b>Direction: </b> Client -> Server */
        public static final Route ADD_TAG = new Route(Method.PUT   , "tickets/{ticket.id}/tags/{tag}", false);
        /** Removes a Discord account from a user. <p><b>Direction: </b> Client -> Server */
        public static final Route DEL_TAG = new Route(Method.DELETE, "tickets/{ticket.id}/tags/{tag}", false);

        /** Adds a Discord account to a user. <p><b>Direction: </b> Client -> Server */
        public static final Route ADD_USER = new Route(Method.PUT   , "tickets/{ticket.id}/users/{user.id}", false);
        /** Removes a Discord account from a user. <p><b>Direction: </b> Client -> Server */
        public static final Route DEL_USER = new Route(Method.DELETE, "tickets/{ticket.id}/users/{user.id}", false);
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

    /** Provides all template {@link Route} objects form this class. */
    public static Route[] getRoutes() {
        return ROUTES;
    }
}
