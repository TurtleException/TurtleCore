package de.turtle_exception.core.core.net.route;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

// TODO: docs
public class Routes {
    public static final Route OK    = new Route(Method.RESPOND, "ok"   , false);
    public static final Route ERROR = new Route(Method.ERROR  , "error", true );
    public static final Route QUIT  = new Route(Method.META   , "quit" , false);

    public static final Route RESPONSE = new Route(Method.RESPOND, "", true);

    public static class Group {
        public static final Route GET     = new Route(Method.GET   , "groups/{group.id}", false);
        public static final Route GET_ALL = new Route(Method.GET   , "groups"           , false);
        public static final Route DEL     = new Route(Method.DELETE, "groups/{group.id}", false);
        public static final Route CREATE  = new Route(Method.PUT   , "groups"           , true );
        public static final Route MODIFY  = new Route(Method.PATCH , "groups/{group.id}", true );

        public static final Route UPDATE = new Route(Method.UPDATE, "groups/{group.id}", true );
        public static final Route REMOVE = new Route(Method.REMOVE, "groups/{group.id}", false);

        public static final Route ADD_USER = new Route(Method.PUT   , "groups/{group}/members/{user}", false);
        public static final Route DEL_USER = new Route(Method.DELETE, "groups/{group}/members/{user}", false);
    }

    public static class User {
        public static final Route GET     = new Route(Method.GET   , "users/{user.id}", false);
        public static final Route GET_ALL = new Route(Method.GET   , "users"          , false);
        public static final Route DEL     = new Route(Method.DELETE, "users/{user.id}", false);
        public static final Route CREATE  = new Route(Method.PUT   , "users"          , true );
        public static final Route MODIFY  = new Route(Method.PATCH , "users/{user.id}", true );

        public static final Route UPDATE = new Route(Method.UPDATE, "users/{user.id}", true );
        public static final Route REMOVE = new Route(Method.REMOVE, "users/{user.id}", false);

        public static final Route ADD_DISCORD = new Route(Method.PUT   , "users/{user.id}/discord/{discord.id}", false);
        public static final Route DEL_DISCORD = new Route(Method.DELETE, "users/{user.id}/discord/{discord.id}", false);

        public static final Route ADD_MINECRAFT = new Route(Method.PUT   , "users/{user.id}/minecraft/{minecraft.id}", false);
        public static final Route DEL_MINECRAFT = new Route(Method.DELETE, "users/{user.id}/minecraft/{minecraft.id}", false);
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
