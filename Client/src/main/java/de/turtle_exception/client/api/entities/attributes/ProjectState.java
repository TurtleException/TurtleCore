package de.turtle_exception.client.api.entities.attributes;

import org.jetbrains.annotations.NotNull;

public enum ProjectState {
    CONCEPT(        (byte) 0, "Concept"    , "\uD83D\uDCDA"),
    PLANNING(       (byte) 1, "Planning"   , "\uD83D\uDCD9"),
    APPLICATION(    (byte) 2, "Application", "\uD83D\uDCD8"),
    RUNNING(        (byte) 3, "Running"    , "\uD83D\uDCD7"),
    STOPPED(        (byte) 4, "Stopped"    , "\uD83D\uDCD5"),
    CANCELED(       (byte) 5, "Cancelled"  , "\uD83D\uDEAB"),
    UNDEFINED(Byte.MAX_VALUE, "Undefined"  , "\u2753");

    private final byte code;
    private final @NotNull String name;
    private final @NotNull String emoji;

    ProjectState(byte code, @NotNull String name, @NotNull String emoji) {
        this.code = code;
        this.name = name;
        this.emoji = emoji;
    }

    public byte getCode() {
        return code;
    }

    public @NotNull String getName() {
        return name;
    }

    public @NotNull String getEmoji() {
        return emoji;
    }

    /* - - - */

    public static @NotNull ProjectState of(byte code) {
        for (ProjectState value : ProjectState.values())
            if (value.getCode() == code)
                return value;
        return ProjectState.UNDEFINED;
    }
}
