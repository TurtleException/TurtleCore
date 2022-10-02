package de.turtle_exception.core.netcore.net.route;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

public class Routes {
    public static final Route OK    = new Route("OK"   , true, ContentType.NONE);
    public static final Route ERROR = new Route("ERROR", true, ContentType.ERROR);
    public static final Route QUIT    = new Route("QUIT"   , true, ContentType.NONE);

    public static class Content {
        public static class User {
            public static final Route GET_ALL = new Route("USER GET ALL", false, ContentType.NONE);
            public static final Route GET     = new Route("USER GET"    , false, ContentType.PLAINTEXT);
            public static final Route DEL     = new Route("USER DEL"    , false, ContentType.PLAINTEXT);
            public static final Route SET     = new Route("USER SET"    , true , ContentType.USER);
            public static final Route UPDATE  = new Route("USER UPDATE" , true , ContentType.USER);
            public static final Route UPDATES = new Route("USER UPDATES", true , ContentType.USERS);

            public static final Route MODIFY_NAME = new Route("USER MOD NAME", true, ContentType.PLAINTEXT);

            public static final Route GROUP_JOIN  = new Route("USER GROUP JOIN" , true, ContentType.PLAINTEXT);
            public static final Route GROUP_LEAVE = new Route("USER GROUP LEAVE", true, ContentType.PLAINTEXT);
        }

        public static class Group {
            public static final Route GET_ALL = new Route("GROUP GET ALL", false, ContentType.NONE);
            public static final Route GET     = new Route("GROUP GET"    , false, ContentType.PLAINTEXT);
            public static final Route DEL     = new Route("GROUP DEL"    , false, ContentType.PLAINTEXT);
            public static final Route SET     = new Route("GROUP SET"    , true , ContentType.GROUP);
            public static final Route UPDATE  = new Route("GROUP UPDATE" , true , ContentType.GROUP);
            public static final Route UPDATES = new Route("GROUP UPDATES", true , ContentType.GROUPS);

            public static final Route MODIFY_NAME = new Route("GROUP MOD NAME", true, ContentType.PLAINTEXT);
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
