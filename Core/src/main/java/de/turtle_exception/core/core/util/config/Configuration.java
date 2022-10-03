package de.turtle_exception.core.core.util.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Properties;

// TODO: docs
/**
 * A simple wrapper for the {@link Properties} class. This class supports type-parsing and primitive data.
 */
public class Configuration {
    private final Properties properties;

    public Configuration() {
        this.properties = new Properties();
    }

    public Configuration(@NotNull Properties properties) {
        this.properties = properties;
    }

    public @NotNull Properties getProperties() {
        return this.properties;
    }

    /* - - - */

    public @Nullable Object get(@NotNull String key) {
        return properties.get(key);
    }

    public @NotNull Object getOrDefault(@NotNull String key, @NotNull Object def) {
        Object val = this.get(key);
        return val != null ? val : def;
    }

    public @Nullable String getString(@NotNull String key) {
        return properties.getProperty(key);
    }

    public int getInt(@NotNull String key) throws NullPointerException, NumberFormatException {
        String s = getString(key);
        if (s == null)
            throw new NullPointerException();
        return Integer.parseInt(s);
    }

    public int getInt(@NotNull String key, int def) {
        try {
            return this.getInt(key);
        } catch (NullPointerException | NumberFormatException ignored) {
            return def;
        }
    }

    public long getLong(@NotNull String key) throws NullPointerException, NumberFormatException {
        String s = getString(key);
        if (s == null)
            throw new NullPointerException();
        return Long.parseLong(s);
    }

    public long getLong(@NotNull String key, long def) {
        try {
            return this.getLong(key);
        } catch (NullPointerException | NumberFormatException ignored) {
            return def;
        }
    }

    public short getShort(@NotNull String key) throws NullPointerException, NumberFormatException {
        String s = getString(key);
        if (s == null)
            throw new NullPointerException();
        return Short.parseShort(s);
    }

    public short getShort(@NotNull String key, short def) {
        try {
            return this.getShort(key);
        } catch (NullPointerException | NumberFormatException ignored) {
            return def;
        }
    }

    public byte getByte(@NotNull String key) throws NullPointerException, NumberFormatException {
        String s = getString(key);
        if (s == null)
            throw new NullPointerException();
        return Byte.parseByte(s);
    }

    public byte getByte(@NotNull String key, byte def) {
        try {
            return this.getByte(key);
        } catch (NullPointerException | NumberFormatException ignored) {
            return def;
        }
    }
}
