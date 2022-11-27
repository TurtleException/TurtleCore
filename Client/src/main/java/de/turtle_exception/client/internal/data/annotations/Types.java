package de.turtle_exception.client.internal.data.annotations;

public class Types {
    private Types() { }

    private static class Templates {
        public static final String TURTLE    = "TURTLE";
        public static final String SNOWFLAKE = "BIGINT(20)";
        public static final String UUID      = "VARCHAR(36)";
    }

    /* - - - */

    public static class Group {
        public static final String NAME    = "TINYTEXT";
        public static final String MEMBERS = Templates.TURTLE;
    }

    public static class Ticket {
        public static final String STATE           = "TINYINT(4)";
        public static final String TITLE           = "TINYTEXT";
        public static final String CATEGORY        = "TINYTEXT";
        public static final String TAGS            = "TINYTEXT";
        public static final String DISCORD_CHANNEL = Templates.SNOWFLAKE;
        public static final String USERS           = Templates.TURTLE;
    }

    public static class Turtle {
        public static final String ID = "BIGINT(20)";
    }

    public static class User {
        public static final String NAME      = "TINYTEXT";
        public static final String DISCORD   = Templates.SNOWFLAKE;
        public static final String MINECRAFT = Templates.UUID;
    }
}
