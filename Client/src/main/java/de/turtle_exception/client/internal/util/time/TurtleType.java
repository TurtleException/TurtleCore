package de.turtle_exception.client.internal.util.time;

public enum TurtleType {
    UNKNOWN(0),
    PACKET(1),
    CONVERSATION(2),
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
}
