package de.turtle_exception.client.internal.net.packets;

import de.turtle_exception.client.internal.net.Connection;
import de.turtle_exception.client.internal.net.Direction;
import de.turtle_exception.client.internal.util.MathUtil;
import de.turtle_exception.client.internal.util.crypto.Encryption;
import de.turtle_exception.client.internal.util.time.TurtleUtil;
import org.jetbrains.annotations.NotNull;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public final class CompiledPacket {
    public static final int META_BYTES = 17;

    private final byte[] bytes;
    private final byte[] content;

    /* - META INFO - */
    private final long turtle;
    private final long responseCode;
    private final byte type;
    /* ---  ---  --- */

    private final @NotNull Direction  direction;
    private final @NotNull Connection connection;

    private final long deadline;

    public CompiledPacket(final byte[] bytes, @NotNull Direction direction, @NotNull Connection connection, long deadline) throws IllegalArgumentException {
        if (bytes.length < META_BYTES)
            throw new IllegalArgumentException("Missing packet information: " + bytes.length + " of " + META_BYTES + " bytes present.");

        this.bytes      = bytes;
        this.direction  = direction;
        this.connection = connection;
        this.deadline   = deadline;

        this.turtle       = MathUtil.bytesToLong(bytes, 0);
        this.responseCode = MathUtil.bytesToLong(bytes, Long.BYTES);
        this.type         = bytes[Long.BYTES * 2];

        this.content = new byte[bytes.length - META_BYTES];
        System.arraycopy(bytes, META_BYTES, content, 0, content.length);
    }

    public CompiledPacket(final byte[] content, @NotNull Direction direction, @NotNull Connection connection, final long deadline, final long id, final long responseCode, final byte type) {
        this(buildBytes(content, id, responseCode, type), direction, connection, deadline);
    }

    private static byte[] buildBytes(byte[] content, long id, long conversation, byte type) {
        byte[] bytes = new byte[content.length + META_BYTES];
        System.arraycopy(content, 0, bytes, META_BYTES, content.length);
        System.arraycopy(MathUtil.longToBytes(id), 0, bytes, 0, Long.BYTES);
        System.arraycopy(MathUtil.longToBytes(conversation), 0, bytes, Long.BYTES, Long.BYTES);
        bytes[Long.BYTES * 2] = type;
        return bytes;
    }

    /* - - - */

    public @NotNull Packet toPacket() throws IllegalStateException {
        return this.doToPacket(this.content);
    }

    public @NotNull Packet toPacket(@NotNull String pass)
            throws IllegalStateException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        byte[] decrypted = Encryption.decrypt(this.content, pass);
        return this.doToPacket(decrypted);
    }

    private @NotNull Packet doToPacket(byte[] bytes) {
        if (type == HandshakePacket.TYPE)
            return new HandshakePacket(turtle, deadline, connection, responseCode, direction, bytes);

        if (type == DataPacket.TYPE)
            return new DataPacket(turtle, deadline, connection, responseCode, direction, bytes);

        if (type == HeartbeatPacket.TYPE)
            return new HeartbeatPacket(turtle, deadline, connection, responseCode, direction, bytes);

        if (type == ErrorPacket.TYPE)
            return new ErrorPacket(turtle, deadline, connection, responseCode, bytes);

        throw new IllegalStateException("Illegal packet type: " + type);
    }

    /* - - - */

    public byte[] getBytes() {
        return bytes;
    }

    /** Returns the bytes of this message without meta information */
    public byte[] getContent() {
        return content;
    }

    public long getId() {
        return turtle;
    }

    public long getTime() {
        return TurtleUtil.getTime(getId());
    }

    public long getDeadline() {
        return deadline;
    }

    public long getResponseCode() {
        return responseCode;
    }

    public byte getTypeId() {
        return type;
    }

    public @NotNull Direction getDirection() {
        return direction;
    }

    public @NotNull Connection getConnection() {
        return connection;
    }
}
