package de.turtle_exception.client.api.entities.attributes;

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
}
