package de.turtle_exception.core.netcore.net.route;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

public class Routes {
    public static final Route OK = new Route("OK", true, ContentType.NONE);

    public static class Login {
        public static final Route LOGIN   = new Route("LOGIN"  , false, ContentType.USERNAME);
        public static final Route VERSION = new Route("VERSION", false, ContentType.VERSION);
        public static final Route QUIT    = new Route("QUIT"   , true, ContentType.NONE);
    }

    public static class Content {
        public static class User {
            public static final Route GET_ALL = new Route("USER GET ALL", false, ContentType.NONE);
            public static final Route GET     = new Route("USER GET"    , false, ContentType.TURTLE_ID);
            public static final Route DEL     = new Route("USER DEL"    , false, ContentType.TURTLE_ID);
            public static final Route UPDATE  = new Route("USER UPDATE" , true , ContentType.USER);

            public static final Route RAW  = new Route(null, true, ContentType.USER);
            public static final Route RAWS = new Route(null, true, ContentType.USERS);
        }

        public static class Group {
            public static final Route GET_ALL = new Route("GROUP GET ALL", false, ContentType.NONE);
            public static final Route GET     = new Route("GROUP GET"    , false, ContentType.TURTLE_ID);
            public static final Route DEL     = new Route("GROUP DEL"    , false, ContentType.TURTLE_ID);
            public static final Route UPDATE  = new Route("GROUP UPDATE" , true , ContentType.GROUP);

            public static final Route RAW  = new Route(null, true, ContentType.GROUP);
            public static final Route RAWS = new Route(null, true, ContentType.GROUPS);
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
