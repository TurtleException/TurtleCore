package de.turtle_exception.client.internal.net.packets;

import de.turtle_exception.client.internal.net.Connection;
import de.turtle_exception.client.internal.net.Direction;
import de.turtle_exception.client.internal.util.time.TurtleType;
import de.turtle_exception.client.internal.util.time.TurtleUtil;
import org.jetbrains.annotations.NotNull;

public class HandshakePacket extends Packet {
    public static final byte TYPE = 0;

    protected final @NotNull String msg;

    public HandshakePacket(long id, long deadline, @NotNull Connection connection, long responseCode, @NotNull Direction direction, byte[] bytes) {
        this(id, deadline, connection, responseCode, direction, new String(bytes));
    }

    public HandshakePacket(long id, long deadline, @NotNull Connection connection, long responseCode, @NotNull Direction direction, @NotNull String msg) {
        super(id, deadline, connection, responseCode, direction, TYPE);
        this.msg = msg;
    }

    /** Sends a new message (response) */
    public HandshakePacket(long deadline, @NotNull Connection connection, long responseCode, @NotNull String str) {
        this(TurtleUtil.newId(TurtleType.PACKET), deadline, connection, responseCode, Direction.OUTBOUND, str);
    }

    /** Sends a new message (not a response) */
    public HandshakePacket(long deadline, @NotNull Connection connection, @NotNull String str) {
        this(deadline, connection, TurtleUtil.newId(TurtleType.RESPONSE_CODE), str);
    }

    @Override
    public byte[] getBytes() {
        return msg.getBytes();
    }

    public @NotNull String getMessage() {
        return msg;
    }
}
