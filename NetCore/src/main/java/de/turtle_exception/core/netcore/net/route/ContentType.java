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
    /** A user id with an info field (separated by a space) */
    USER_INFO("userinfo"),

    USER_DISCORD("discord"),
    USER_MINECRAFT("minecraft"),

    GROUP("group"),
    GROUPS("groups"),
    /** A group id with an info field (separated by a space) */
    GROUP_INFO("groupinfo")
    ;

    private final String name;

    ContentType(@NotNull String name) {
        this.name = name;
    }

    public @NotNull String getName() {
        return name;
    }
}
