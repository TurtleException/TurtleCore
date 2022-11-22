package de.turtle_exception.client.internal.data.annotations;

public class Keys {
    private Keys() { }

    public static class Group {
        public static final String NAME    = "name";
        public static final String MEMBERS = "users";
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
