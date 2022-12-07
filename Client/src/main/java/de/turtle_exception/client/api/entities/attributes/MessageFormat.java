package de.turtle_exception.client.api.entities.attributes;

import de.turtle_exception.client.api.entities.messages.SyncMessage;
import org.jetbrains.annotations.NotNull;

/** Marks the format of a {@link SyncMessage}. */
public enum MessageFormat {
    /** Raw format; The message is not formatted. */
    NONE((byte) 0),
    /** A custom format that preserves as much information from other formats as possible. <br> Used to store messages. */
    TURTLE((byte) 1),
    /**
     * Discord Markdown format as specified by
     * <a href="https://support.discord.com/hc/en-us/articles/210298617">Discord Support: Markdown Text 101</a>.
     */
    MARKDOWN((byte) 2),
    /**
     * Minecraft legacy color codes as specified by
     * <a href="https://minecraft.fandom.com/wiki/Formatting_codes">Minecraft Wiki: Formatting codes</a>.
     */
    MINECRAFT_LEGACY((byte) 3),
    /**
     * Minecraft JSON format as specified by
     * <a href="https://minecraft.fandom.com/wiki/Raw_JSON_text_format">Minecraft Wiki: Raw JSON text format</a>.
     */
    MINECRAFT_JSON((byte) 4),
    /**
     * Undefined formatting. Usually used by {@link MessageFormat#of(byte)} when a {@code byte} could not be parsed to a
     * specific MessageFormat.
     */
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
