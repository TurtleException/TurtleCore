package de.turtle_exception.client.internal.net.message;

import org.jetbrains.annotations.Nullable;

public enum Route {
    DATA(0),
    ERROR(1),
    QUIT(2),
    HEARTBEAT(3),
    HEARTBEAT_ACK(4),
    OK(5)
    ;

    public final int code;
    Route(int code) { this.code = code; }

    public static @Nullable Route of(int code) {
        for (Route value : values())
            if (value.code == code)
                return value;
        return null;
    }
}
