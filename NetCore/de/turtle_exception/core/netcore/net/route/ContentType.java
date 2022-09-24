package de.turtle_exception.core.netcore.net.route;

import org.jetbrains.annotations.NotNull;

// TODO: docs
// TODO: replace USERNAME, VERSION and TURTLE_ID with PLAINTEXT ?
// TODO: can't this implicetely be passed vie the Route?
public enum ContentType {
    NONE("null"),

    USERNAME("username"),
    VERSION("version"),
    TURTLE_ID("id"),

    USER("user"),
    USERS("users"),

    GROUP("group"),
    GROUPS("groups")
    ;

    private final String name;

    ContentType(@NotNull String name) {
        this.name = name;
    }

    public @NotNull String getName() {
        return name;
    }
}
