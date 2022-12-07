package de.turtle_exception.client.internal.data.annotations;

public class Keys {
    private Keys() { }

    /* - ATTRIBUTES - */

    public static class Attribute {
        public static class EphemeralType {
            public static final String EPHEMERAL = "ephemeral";
        }
    }

    /* - PACKAGES - */

    public static class Form {
        public static class CompletedForm {
            public static final String FORM            = "form";
            public static final String AUTHOR          = "author";
            public static final String TIME_SUBMISSION = "time_submission";
            public static final String QUERY_RESPONSES = "query_responses";
        }

        public static class Element {
            public static final String TITLE = "title";
        }

        public static class QueryElement {
            public static final String DESCRIPTION  = "description";
            public static final String CONTENT_TYPE = "content_type";
            public static final String REQUIRED     = "required";
        }

        public static class QueryResponse {
            public static final String QUERY   = "query";
            public static final String CONTENT = "content";
        }

        public static class TemplateForm {
            public static final String TITLE    = "title";
            public static final String ELEMENTS = "elements";
        }

        public static class TextElement {
            public static final String CONTENT = "content";
        }
    }

    public static class Messages {
        public static class DiscordChannel {
            public static final String SNOWFLAKE = "snowflake";
        }

        public static class IChannel {
            public static final String SYNC_CHANNEL = "sync_channel";
        }

        public static class MinecraftChannel {
            public static final String TYPE         = "type";
            public static final String IDENTIFIER   = "identifier";
        }

        public static class SyncChannel {
            public static final String DISCORD   = "discord";
            public static final String MINECRAFT = "minecraft";
        }

        public static class SyncMessage {
            public static final String FORMAT    = "format";
            public static final String AUTHOR    = "author";
            public static final String CONTENT   = "content";
            public static final String REFERENCE = "reference";
            public static final String CHANNEL   = "channel";
            public static final String SOURCE    = "source";
        }
    }

    /* - - - */

    public static class Group {
        public static final String NAME    = "name";
        public static final String MEMBERS = "users";
    }

    public static class JsonResource {
        public static final String IDENTIFIER = "identifier";
        public static final String CONTENT    = "content";
    }

    public static class Project {
        public static final String TITLE        = "title";
        public static final String CODE         = "code";
        public static final String STATE        = "state";
        public static final String MEMBERS      = "users";
        public static final String APP_FORM     = "application_form";
        public static final String TIME_RELEASE = "time_release";
        public static final String TIME_APPLY   = "time_apply";
        public static final String TIME_START   = "time_start";
        public static final String TIME_END     = "time_end";
    }

    public static class Ticket {
        public static final String STATE           = "state";
        public static final String TITLE           = "title";
        public static final String CATEGORY        = "category";
        public static final String TAGS            = "tags";
        public static final String DISCORD_CHANNEL = "discord_channel";
        public static final String USERS           = "users";
    }

    public static class Turtle {
        public static final String ID = "id";
    }

    public static class User {
        public static final String NAME      = "name";
        public static final String DISCORD   = "discord";
        public static final String MINECRAFT = "minecraft";
    }
}
