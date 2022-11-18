package de.turtle_exception.client.internal.net;

import org.jetbrains.annotations.Nullable;

public enum DataMethod {
    DELETE(0),
    GET(1),
    PUT(2),
    PATCH(3),
    PATCH_ENTRY_ADD(4),
    PATCH_ENTRY_DEL(5),
    UPDATE(6),
    REMOVE(7),

    ;

    public final int code;
    DataMethod(int code) { this.code = code; }

    public static @Nullable DataMethod of(int code) {
        for (DataMethod value : values())
            if (value.code == code)
                return value;
        return null;
    }

    public static @Nullable DataMethod of(String name) {
        for (DataMethod value : values())
            if (value.name().equals(name))
                return value;
        return null;
    }
}
