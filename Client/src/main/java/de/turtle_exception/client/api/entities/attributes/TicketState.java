package de.turtle_exception.client.api.entities.attributes;

import org.jetbrains.annotations.NotNull;

public enum TicketState {
    CLOSED((byte) 0, "Closed", "\uD83D\uDD34"),

    OPEN((byte) 1, "Open", "\uD83D\uDFE1"),

    DRAFT((byte) 2, "Draft", "\uD83D\uDFE0"),

    SOLVED((byte) 3, "Solved", "\uD83D\uDFE2"),

    UNDEFINED(Byte.MAX_VALUE, "Undefined", "\uD83D\uDFE3");

    private final byte code;
    private final String title;
    private final String emoji;

    TicketState(byte code, @NotNull String title, @NotNull String emoji) {
        this.code  = code;
        this.title = title;
        this.emoji = emoji;
    }

    public byte getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public String getEmoji() {
        return emoji;
    }

    /* - - - */

    public static @NotNull TicketState of(byte code) {
        for (TicketState value : TicketState.values())
            if (value.getCode() == code)
                return value;
        return TicketState.UNDEFINED;
    }
}
