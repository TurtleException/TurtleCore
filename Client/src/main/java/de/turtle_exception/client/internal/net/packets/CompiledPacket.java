package de.turtle_exception.client.internal.net.packets;

import de.turtle_exception.client.internal.net.Connection;
import de.turtle_exception.client.internal.net.Direction;
import de.turtle_exception.client.internal.util.MathUtil;
import de.turtle_exception.client.internal.util.time.TurtleUtil;
import org.jetbrains.annotations.NotNull;

public final class CompiledPacket {
    public static final int META_BYTES = 8;

    private final byte[] bytes;

    /* - META INFO - */
    private final long turtle;
    private final long conversation;
    /* ---  ---  --- */

    private final @NotNull Direction  direction;
    private final @NotNull Connection connection;

    public CompiledPacket(final byte[] bytes, @NotNull Direction direction, @NotNull Connection connection) throws IllegalArgumentException {
        if (bytes.length < META_BYTES)
            throw new IllegalArgumentException("Missing packet information: " + bytes.length + " of " + META_BYTES + "bytes present.");

        this.bytes      = bytes;
        this.direction  = direction;
        this.connection = connection;

        this.turtle       = MathUtil.bytesToLong(bytes, 0);
        this.conversation = MathUtil.bytesToLong(bytes, Long.BYTES);
    }

    public CompiledPacket(final byte[] content, @NotNull Direction direction, @NotNull Connection connection, final long id, final long conversation) {
        this(buildBytes(content, id, conversation), direction, connection);
    }

    private static byte[] buildBytes(byte[] content, long id, long conversation) {
        byte[] bytes = new byte[content.length + META_BYTES];
        System.arraycopy(content, 0, bytes, META_BYTES, content.length);
        System.arraycopy(MathUtil.longToBytes(id), 0, bytes, 0, Long.BYTES);
        System.arraycopy(MathUtil.longToBytes(conversation), 0, bytes, Long.BYTES, Long.BYTES);
        return bytes;
    }

    /* - - - */

    public byte[] getBytes() {
        return bytes;
    }

    public long getId() {
        return turtle;
    }

    public long getTime() {
        return TurtleUtil.getTime(getId());
    }

    public long getConversation() {
        return conversation;
    }

    public @NotNull Direction getDirection() {
        return direction;
    }

    public @NotNull Connection getConnection() {
        return connection;
    }
}
