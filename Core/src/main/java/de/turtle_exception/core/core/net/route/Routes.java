package de.turtle_exception.core.core.net.route;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

// TODO: docs
public class Routes {
    public static final Route OK    = new Route(Method.RESPOND, "ok"   , false);
    public static final Route ERROR = new Route(Method.ERROR  , "error", true );
    public static final Route QUIT  = new Route(Method.META   , "quit" , false);

    public static final Route EMPTY_RESPONSE = new Route(Method.RESPOND, "", true);

    public static class Group {
        public static final Route GET    = new Route(Method.GET   , "groups/{id}", false);
        public static final Route DEL    = new Route(Method.DELETE, "groups/{id}", false);
        public static final Route CREATE = new Route(Method.PUT   , "groups"     , true );
        public static final Route MODIFY = new Route(Method.PATCH , "groups/{id}", true );
    }

    public static class User {
        public static final Route GET    = new Route(Method.GET   , "users/{id}", false);
        public static final Route DEL    = new Route(Method.DELETE, "users/{id}", false);
        public static final Route CREATE = new Route(Method.PUT   , "users"     , true );
        public static final Route MODIFY = new Route(Method.PATCH , "users/{id}", true );
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
