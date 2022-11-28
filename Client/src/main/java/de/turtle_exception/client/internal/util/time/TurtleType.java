package de.turtle_exception.client.internal.util.time;

import de.turtle_exception.client.api.entities.Group;
import de.turtle_exception.client.api.entities.Ticket;
import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.api.entities.User;
import org.jetbrains.annotations.NotNull;

public enum TurtleType {
    UNKNOWN(0),
    PACKET(1),
    RESPONSE_CODE(2),
    RESOURCE_GENERIC(3),
    RESOURCE_GROUP(4),
    RESOURCE_TICKET(5),
    RESOURCE_USER(6),

    ;

    private final byte b;

    TurtleType(int b) {
        this.b = (byte) b;
    }

    public byte getAsByte() {
        return b;
    }

    /* - - - */

    public static @NotNull TurtleType ofResource(@NotNull Class<? extends Turtle> type) {
        if (type.isAssignableFrom(Group.class))
            return RESOURCE_GROUP;
        if (type.isAssignableFrom(Ticket.class))
            return RESOURCE_TICKET;
        if (type.isAssignableFrom(User.class))
            return RESOURCE_USER;
        return RESOURCE_GENERIC;
    }
}
