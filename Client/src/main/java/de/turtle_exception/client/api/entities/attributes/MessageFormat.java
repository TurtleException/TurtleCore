package de.turtle_exception.client.api.entities.attributes;

import de.turtle_exception.client.api.entities.messages.SyncMessage;
import org.jetbrains.annotations.NotNull;

/** Marks the format of a {@link SyncMessage}. */
public enum MessageFormat {
    NONE(     (byte) 0),
    TURTLE(   (byte) 1),
    MARKDOWN( (byte) 2),
    MINECRAFT((byte) 3),
    UNDEFINED(Byte.MAX_VALUE);

    private final byte code;

    MessageFormat(byte code) {
        this.code = code;
    }

    /**
     * Returns the {@code byte} code that is unique to this MessageFormat.
     * @return Unique code.
     */
    public byte getCode() {
        return code;
    }

    /**
     * Attempts to parse the provided {@code byte} into its corresponding {@link MessageFormat}. If no MessageFormat with
     * that code exists, {@link MessageFormat#UNDEFINED} is returned.
     * @param code Code of the MessageFormat.
     * @return The parsed MessageFormat, or {@link MessageFormat#UNDEFINED} as default.
     */
    public static @NotNull MessageFormat of(byte code) {
        for (MessageFormat value : MessageFormat.values())
            if (value.getCode() == code)
                return value;
        return MessageFormat.UNDEFINED;
    }
}
