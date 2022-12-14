package de.turtle_exception.client.internal.util.time;

public enum TurtleType {
    UNKNOWN(0),
    PACKET(1),
    RESPONSE_CODE(2),
    RESOURCE(3),

    ;

    private final byte b;

    TurtleType(int b) {
        this.b = (byte) b;
    }

    public byte getAsByte() {
        return b;
    }
}
