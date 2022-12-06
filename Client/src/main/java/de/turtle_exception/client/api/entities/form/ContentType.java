package de.turtle_exception.client.api.entities.form;

import org.jetbrains.annotations.NotNull;

/**
 * Marks the type of content a {@link QueryElement} expects as input.
 * @see QueryElement#getContentType()
 */
public enum ContentType {
    SHORT_TEXT((byte) 0, "Short text",  String.class),
    LONG_TEXT( (byte) 1, "Long text" ,  String.class),
    INTEGER(   (byte) 2, "Integer"   , Integer.class),
    DECIMAL(   (byte) 3, "Decimal"   ,  Double.class),
    BOOLEAN(   (byte) 4, "Boolean"   , Boolean.class);

    private final byte code;
    private final String title;
    private final Class<?> type;

    ContentType(byte code, @NotNull String title, @NotNull Class<?> type) {
        this.code = code;
        this.title = title;
        this.type = type;
    }

    /**
     * Returns the {@code byte} code that is unique to this ContentType.
     * @return Unique code.
     */
    public byte getCode() {
        return code;
    }

    /**
     * Returns a String representation of this ContentType.
     * @return Title String.
     */
    public @NotNull String getTitle() {
        return title;
    }

    /**
     * Return the Java representation of this ContentType.
     * @return Java type class.
     */
    public @NotNull Class<?> getType() {
        return type;
    }

    /* - - - */

    /**
     * Attempts to parse the provided {@code byte} into its corresponding {@link ContentType}. If no ProjectState with
     * that code exists, {@link ContentType#LONG_TEXT} is returned, as it is the default used to store query data in the
     * backing database and can represent any other data type lossless.
     * @param code Code of the ContentType.
     * @return The parsed ContentType, or {@link ContentType#LONG_TEXT} as default.
     */
    public static @NotNull ContentType of(byte code) {
        for (ContentType value : ContentType.values())
            if (value.getCode() == code)
                return value;
        return ContentType.LONG_TEXT;
    }
}
