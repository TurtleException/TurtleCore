package de.turtle_exception.core.core.net.route;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

public class Routes {
    public static final Route OK    = new Route("OK"   , true, ContentType.NONE);
    public static final Route ERROR = new Route("ERROR", true, ContentType.ERROR);
    public static final Route QUIT  = new Route("QUIT" , true, ContentType.NONE);

    public static class Content {
        public static class User {
            /** Requests all raw user objects (without relational data). */
            public static final Route GET_ALL   = new Route("USER GET ALL"  , false, ContentType.NONE);
            /** Response to {@link Routes.Content.User#GET_ALL}. */
            public static final Route GET_ALL_R = new Route("USER GET ALL R", true , ContentType.USERS);
            /** Requests the raw user object (without relational data). */
            public static final Route GET       = new Route("USER GET"      , false, ContentType.PLAINTEXT);
            /** Response to {@link Routes.Content.User#GET}. */
            public static final Route GET_R     = new Route("USER GET R"    , true , ContentType.USER);
            /** Requests the permanent deletion of a user. */
            public static final Route DEL       = new Route("USER DEL"      , false, ContentType.PLAINTEXT);

            /** Informs the client that a user has been deleted and its cache should be updated. */
            public static final Route DELETED = new Route("USER DELETED", true , ContentType.PLAINTEXT);
            /** Informs the client that a user has been updated. */
            public static final Route UPDATE  = new Route("USER UPDATE" , true , ContentType.USER);
            /** Informs the client that multiple users have been updated (response to GET_ALL). */
            public static final Route UPDATES = new Route("USER UPDATES", true , ContentType.USERS);

            /** Requests to modify the name of a user. */
            public static final Route MOD_NAME = new Route("USER MOD NAME", false, ContentType.USER_INFO);

            /** Requests the list of group ids associated with a user. */
            public static final Route GROUPS_GET    = new Route("USER GROUP GET"   , false, ContentType.PLAINTEXT);
            /** Response to {@link Routes.Content.User#GROUPS_GET}. */
            public static final Route GROUPS_GET_R  = new Route("USER GROUP GET R" , true , ContentType.USER_GROUPS);
            /** Requests to add a user to a group (group id will be saved with the user). */
            public static final Route GROUPS_ADD    = new Route("USER GROUP ADD"   , false, ContentType.USER_GROUPS);
            /** Requests to remove a user from a group. */
            public static final Route GROUPS_DEL    = new Route("USER GROUP DEL"   , false, ContentType.USER_GROUPS);
            /** Informs the client that the list of group ids associated with a user has been updated. */
            public static final Route GROUPS_UPDATE = new Route("USER GROUP UPDATE", true , ContentType.USER_GROUPS);

            /** Requests the list of discord ids associated with a user. */
            public static final Route DISCORD_GET    = new Route("USER DISCORD GET"   , false, ContentType.PLAINTEXT);
            /** Response to {@link Routes.Content.User#DISCORD_GET}. */
            public static final Route DISCORD_GET_R  = new Route("USER DISCORD GET R" , true , ContentType.USER_DISCORD);
            /** Requests to add a discord id to a user. */
            public static final Route DISCORD_ADD    = new Route("USER DISCORD ADD"   , false, ContentType.USER_DISCORD);
            /** Requests to remove a discord id from a user. */
            public static final Route DISCORD_DEL    = new Route("USER DISCORD DEL"   , false, ContentType.USER_DISCORD);
            /** Informs the client that the list of discord ids associated with a user has been updated. */
            public static final Route DISCORD_UPDATE = new Route("USER DISCORD UPDATE", true , ContentType.USER_DISCORD);

            /** Requests the list of minecraft ids associated with a user. */
            public static final Route MINECRAFT_GET    = new Route("USER MINECRAFT GET"   , false, ContentType.PLAINTEXT);
            /** Response to {@link Routes.Content.User#MINECRAFT_GET}. */
            public static final Route MINECRAFT_GET_R  = new Route("USER MINECRAFT GET R" , true , ContentType.USER_MINECRAFT);
            /** Requests to add a minecraft id to a user. */
            public static final Route MINECRAFT_ADD    = new Route("USER MINECRAFT ADD"   , false, ContentType.USER_MINECRAFT);
            /** Requests to remove a minecraft id from a user. */
            public static final Route MINECRAFT_DEL    = new Route("USER MINECRAFT DEL"   , false, ContentType.USER_MINECRAFT);
            /** Informs the client that the list of minecraft ids associated with a user has been updated. */
            public static final Route MINECRAFT_UPDATE = new Route("USER MINECRAFT UPDATE", true , ContentType.USER_MINECRAFT);
        }

        public static class Group {
            /** Requests all raw group objects (without relational data). */
            public static final Route GET_ALL   = new Route("GROUP GET ALL"  , false, ContentType.NONE);
            /** Response to {@link Routes.Content.Group#GET_ALL}. */
            public static final Route GET_ALL_R = new Route("GROUP GET ALL R", true , ContentType.GROUPS);
            /** Requests the raw group object (without relational data). */
            public static final Route GET       = new Route("GROUP GET"      , false, ContentType.PLAINTEXT);
            /** Response to {@link Routes.Content.Group#GET}. */
            public static final Route GET_R     = new Route("GROUP GET R"    , true , ContentType.GROUP);
            /** Requests the permanent deletion of a group. */
            public static final Route DEL       = new Route("GROUP DEL"      , false, ContentType.PLAINTEXT);

            /** Informs the client that a group has been deleted and its cache should be updated. */
            public static final Route DELETED = new Route("GROUP DELETED", true , ContentType.PLAINTEXT);
            /** Informs the client that a group has been updated. */
            public static final Route UPDATE  = new Route("GROUP UPDATE" , true , ContentType.GROUP);
            /** Informs the client that multiple groups have been updated (response to GET_ALL). */
            public static final Route UPDATES = new Route("GROUP UPDATES", true , ContentType.GROUPS);

            /** Requests to modify the name of a group */
            public static final Route MOD_NAME = new Route("GROUP MOD NAME", false, ContentType.GROUP_INFO);
        }
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
