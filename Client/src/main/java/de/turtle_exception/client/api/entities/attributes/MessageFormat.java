package de.turtle_exception.client.api.entities.attributes;

import org.jetbrains.annotations.NotNull;

public enum MessageFormat {
    UNKNOWN((byte) 0),
    NONE((byte) 1),
    MARKDOWN((byte) 2),
    MINECRAFT((byte) 3);

    private final byte code;

    MessageFormat(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static @NotNull MessageFormat of(byte code) {
        for (MessageFormat value : MessageFormat.values())
            if (value.getCode() == code)
                return value;
        return MessageFormat.UNKNOWN;
    }
}
