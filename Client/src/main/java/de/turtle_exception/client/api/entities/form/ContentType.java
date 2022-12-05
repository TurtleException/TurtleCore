package de.turtle_exception.client.api.entities.form;

import org.jetbrains.annotations.NotNull;

public enum ContentType {
    SHORT_TEXT((byte) 0, "Short text", String.class),
    LONG_TEXT((byte) 1, "Long text", String.class),
    INTEGER((byte) 2, "Integer", Integer.class),
    DECIMAL((byte) 3, "Decimal", Double.class),
    BOOLEAN((byte) 4, "Boolean", Boolean.class);

    private final byte code;
    private final String title;
    private final Class<?> type;

    ContentType(byte code, @NotNull String title, @NotNull Class<?> type) {
        this.code = code;
        this.title = title;
        this.type = type;
    }

    public byte getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public Class<?> getType() {
        return type;
    }

    /* - - - */

    public static @NotNull ContentType of(byte code) {
        for (ContentType value : ContentType.values())
            if (value.getCode() == code)
                return value;
        return ContentType.LONG_TEXT;
    }
}
