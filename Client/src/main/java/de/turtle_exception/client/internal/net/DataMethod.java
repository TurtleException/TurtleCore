package de.turtle_exception.client.internal.net;

import org.jetbrains.annotations.Nullable;

public enum DataMethod {
    DELETE(0),
    GET(1),
    PUT(2),
    PATCH(3),
    UPDATE(4),
    REMOVE(5),

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
