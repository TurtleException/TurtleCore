package de.turtle_exception.core.internal.data.tree;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class DataPrimitive extends DataElement {
    private final Object value;

    public DataPrimitive(@NotNull Object obj) {
        this.value = obj;
    }

    public @NotNull Object getObject() {
        return value;
    }

    public boolean getBoolean() throws ClassCastException {
        return (boolean) value;
    }

    public byte getByte() throws ClassCastException {
        return (byte) value;
    }

    public double getDouble() throws ClassCastException {
        return (double) value;
    }

    public float getFloat() throws ClassCastException {
        return (float) value;
    }

    public int getInt() throws ClassCastException {
        return (int) value;
    }

    public long getLong() throws ClassCastException {
        return (long) value;
    }

    public short getShort() throws ClassCastException {
        return (short) value;
    }

    public @NotNull String getString() {
        return String.valueOf(value);
    }
}
