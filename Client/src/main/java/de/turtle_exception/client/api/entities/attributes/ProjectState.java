package de.turtle_exception.client.api.entities.attributes;

import org.jetbrains.annotations.NotNull;

// TODO: emojis
public enum ProjectState {
    CONCEPT((byte) 0, "Concept"),
    PLANNING((byte) 1, "Planning"),
    APPLICATION((byte) 2, "Application"),
    RUNNING((byte) 3, "Running"),
    STOPPED((byte) 4, "Stopped"),
    CANCELED((byte) 5, "Canceled"),
    UNDEFINED(Byte.MAX_VALUE, "Undefined");

    private final byte code;
    private final @NotNull String name;

    ProjectState(byte code, @NotNull String name) {
        this.code = code;
        this.name = name;
    }

    public byte getCode() {
        return code;
    }

    public @NotNull String getName() {
        return name;
    }

    /* - - - */

    public static @NotNull ProjectState of(byte code) {
        for (ProjectState value : ProjectState.values())
            if (value.getCode() == code)
                return value;
        return ProjectState.UNDEFINED;
    }
}
