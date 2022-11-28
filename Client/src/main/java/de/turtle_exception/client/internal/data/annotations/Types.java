package de.turtle_exception.client.internal.data.annotations;

public class Types {
    private Types() { }

    private static class Templates {
        public static final String TURTLE    = "TURTLE";
        public static final String SNOWFLAKE = "BIGINT(20)";
        public static final String UUID      = "VARCHAR(36)";
    }

    /* - PACKAGES - */

    public static class Messages {
        public static class SyncChannel {

        }

        public static class SyncMessage {
            public static final String FORMAT  = "TINYINT";
            public static final String AUTHOR  = Templates.TURTLE;
            public static final String CONTENT = "TEXT";
        }
    }

    /* - - - */

    public static class Group {
        public static final String NAME    = "TINYTEXT";
        public static final String MEMBERS = Templates.TURTLE;
    }

    public static class JsonResource {
        public static final String IDENTIFIER = "TEXT";
        public static final String CONTENT    = "LONGTEXT";
        public static final String EPHEMERAL  = "BOOLEAN";
    }

    public static class Project {
        public static final String TITLE   = "TINYTEXT";
        public static final String CODE    = "TINYTEXT";
        public static final String STATE   = "TINYINT";
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
