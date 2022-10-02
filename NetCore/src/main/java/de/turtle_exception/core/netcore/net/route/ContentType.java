package de.turtle_exception.core.netcore.net.route;

import org.jetbrains.annotations.NotNull;

// TODO: docs
// TODO: can't this implicitly be passed via the Route?
public enum ContentType {
    NONE("null"),

    ERROR("error"),

    PLAINTEXT("plaintext"),

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
