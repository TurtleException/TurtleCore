package de.turtle_exception.client.api.entities.attributes;

import de.turtle_exception.client.api.entities.Project;
import org.jetbrains.annotations.NotNull;

/**
 * Marks the state / status of a {@link Project}.
 * <p> A ProjectState can be serialized using its unique {@code byte} code. It also has a String representation (name)
 * and an appropriate emoji (stored as a String), that can be used to display the ProjectState in a UI.
 * @see Project#getState()
 * @see Project#modifyState(ProjectState)
 */
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

    /**
     * Returns the {@code byte} code that is unique to this ProjectState.
     * @return Unique code.
     */
    public byte getCode() {
        return code;
    }

    /**
     * Returns a String representation of this ProjectState.
     * @return Name String.
     */
    public @NotNull String getName() {
        return name;
    }

    /**
     * Returns an emoji representing this ProjectState as a String.
     * @return Emoji String.
     */
    public @NotNull String getEmoji() {
        return emoji;
    }

    /* - - - */

    /**
     * Attempts to parse the provided {@code byte} into its corresponding {@link ProjectState}. If no ProjectState with
     * that code exists, {@link ProjectState#UNDEFINED} is returned.
     * @param code Code of the ProjectState.
     * @return The parsed ProjectState, or {@link ProjectState#UNDEFINED} as default.
     */
    public static @NotNull ProjectState of(byte code) {
        for (ProjectState value : ProjectState.values())
            if (value.getCode() == code)
                return value;
        return ProjectState.UNDEFINED;
    }
}
