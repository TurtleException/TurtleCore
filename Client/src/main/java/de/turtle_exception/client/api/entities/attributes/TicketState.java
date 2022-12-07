package de.turtle_exception.client.api.entities.attributes;

import de.turtle_exception.client.api.entities.Ticket;
import org.jetbrains.annotations.NotNull;

/**
 * Marks the state / status of a {@link Ticket}.
 * <p> A TicketState can be serialized using its unique {@code byte} code. It also has a String representation (title)
 * and an appropriate emoji (stored as a String), that can be used to display the TicketState in a UI.
 * @see Ticket#getState()
 * @see Ticket#modifyState(TicketState)
 */
public enum TicketState {
    /** Indicating that a Ticket has been closed (not solved). */
    CLOSED((byte) 0, "Closed", "\uD83D\uDD34"),
    /** Indicating that a Ticket is still open. */
    OPEN((byte) 1, "Open", "\uD83D\uDFE1"),
    /** Indicating that a Ticket is not yet open or currently frozen. DRAFT Tickets may only be converted to OPEN or CLOSED */
    DRAFT((byte) 2, "Draft", "\uD83D\uDFE0"),
    /** Indicating that a Ticket has been processed successfully. */
    SOLVED((byte) 3, "Solved", "\uD83D\uDFE2"),
    /**
     * Undefined TicketState. Usually used by {@link TicketState#of(byte)} when a {@code byte} could not be parsed to a
     * specific TicketState.
     */
    UNDEFINED(Byte.MAX_VALUE, "Undefined", "\uD83D\uDFE3");

    private final byte code;
    private final String title;
    private final String emoji;

    TicketState(byte code, @NotNull String title, @NotNull String emoji) {
        this.code  = code;
        this.title = title;
        this.emoji = emoji;
    }

    /**
     * Returns the {@code byte} code that is unique to this TicketState.
     * @return Unique code.
     */
    public byte getCode() {
        return code;
    }

    /**
     * Returns a String representation of this TicketState.
     * @return Title String.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns an emoji representing this TicketState as a String.
     * @return Emoji String.
     */
    public String getEmoji() {
        return emoji;
    }

    /* - - - */

    /**
     * Attempts to parse the provided {@code byte} into its corresponding {@link TicketState}. If no TicketState with
     * that code exists, {@link TicketState#UNDEFINED} is returned.
     * @param code Code of the TicketState.
     * @return The parsed TicketState, or {@link TicketState#UNDEFINED} as default.
     */
    public static @NotNull TicketState of(byte code) {
        for (TicketState value : TicketState.values())
            if (value.getCode() == code)
                return value;
        return TicketState.UNDEFINED;
    }
}
