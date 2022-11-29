package de.turtle_exception.client.internal.data.annotations;

public class Keys {
    private Keys() { }

    /* - PACKAGES - */

    public static class Messages {
        public static class DiscordChannel {

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
        public static final String EPHEMERAL  = "ephemeral";
    }

    public static class Project {
        public static final String TITLE   = "title";
        public static final String CODE    = "code";
        public static final String STATE   = "state";
        public static final String MEMBERS = "users";

        public static class ApplicationForm {
            public static final String PROJECT = "project";
        }

        public static class Application {
            public static final String PROJECT = "project";
            public static final String USER    = "user";
        }
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
