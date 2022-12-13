package de.turtle_exception.client.internal.data.annotations;

public class Types {
    private Types() { }

    private static class Templates {
        public static final String SNOWFLAKE = "BIGINT(20)";
        public static final String UUID      = "VARCHAR(36)";
    }

    /* - ATTRIBUTES - */

    public static class Attributes {
        public static class EphemeralType {
            public static final String EPHEMERAL = "BOOLEAN";
        }
    }

    /* - PACKAGES - */

    public static class Form {
        public static class CompletedForm {
            public static final String FORM            = Turtle.ID;
            public static final String AUTHOR          = Turtle.ID;
            public static final String TIME_SUBMISSION = "BIGINT";
            public static final String QUERY_RESPONSES = Turtle.ID;
        }

        public static class Element {
            public static final String TITLE = "TEXT";
        }

        public static class QueryElement {
            public static final String DESCRIPTION  = "TEXT";
            public static final String CONTENT_TYPE = "TINYINT";
            public static final String REQUIRED     = "BOOLEAN";
        }

        public static class QueryResponse {
            public static final String QUERY   = Turtle.ID;
            public static final String CONTENT = "TEXT";
        }

        public static class TemplateForm {
            public static final String TITLE    = "TEXT";
            public static final String ELEMENTS = Turtle.ID;
        }

        public static class TextElement {
            public static final String CONTENT = "TEXT";
        }
    }

    public static class Messages {
        public static class Attachment {
            public static final String SNOWFLAKE    = Templates.SNOWFLAKE;
            public static final String URL          = "TEXT";
            public static final String PROXY_URL    = "TEXT";
            public static final String FILE_NAME    = "TEXT";
            public static final String CONTENT_TYPE = "TEXT";
            public static final String DESCRIPTION  = "TEXT";
            public static final String SIZE         = "BIGINT";
            public static final String HEIGHT       = "INT";
            public static final String WIDTH        = "INT";
            public static final String EPHEMERAL    = "BOOLEAN";
        }

        public static class DiscordChannel {
            public static final String SNOWFLAKE = Templates.SNOWFLAKE;
        }

        public static class IChannel {
            public static final String SYNC_CHANNEL = Turtle.ID;
        }

        public static class MinecraftChannel {
            public static final String TYPE         = "TINYINT";
            public static final String IDENTIFIER   = "TEXT";
        }

        public static class SyncMessage {
            public static final String FORMAT      = "TINYINT";
            public static final String AUTHOR      = Turtle.ID;
            public static final String CONTENT     = "TEXT";
            public static final String REFERENCE   = Turtle.ID;
            public static final String CHANNEL     = Turtle.ID;
            public static final String SOURCE      = Turtle.ID;
            public static final String ATTACHMENTS = Turtle.ID;
        }
    }

    /* - - - */

    public static class Group {
        public static final String NAME    = "TINYTEXT";
        public static final String MEMBERS = Turtle.ID;
    }

    public static class JsonResource {
        public static final String IDENTIFIER = "TEXT";
        public static final String CONTENT    = "LONGTEXT";
    }

    public static class Project {
        public static final String TITLE        = "TINYTEXT";
        public static final String CODE         = "TINYTEXT";
        public static final String STATE        = "TINYINT";
        public static final String MEMBERS      = Turtle.ID;
        public static final String APP_FORM     = Turtle.ID;
        public static final String TIME_RELEASE = "BIGINT";
        public static final String TIME_APPLY   = "BIGINT";
        public static final String TIME_START   = "BIGINT";
        public static final String TIME_END     = "BIGINT";
    }

    public static class Ticket {
        public static final String STATE           = "TINYINT(4)";
        public static final String TITLE           = "TINYTEXT";
        public static final String CATEGORY        = "TINYTEXT";
        public static final String TAGS            = "TINYTEXT";
        public static final String DISCORD_CHANNEL = Templates.SNOWFLAKE;
        public static final String USERS           = Turtle.ID;
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
