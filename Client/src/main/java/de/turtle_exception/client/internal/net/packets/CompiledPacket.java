package de.turtle_exception.client.internal.net.packets;

import de.turtle_exception.client.internal.net.Connection;
import de.turtle_exception.client.internal.net.Direction;
import de.turtle_exception.client.internal.net.message.Conversation;
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
    public static final int META_BYTES = 9;

    private final byte[] bytes;

    /* - META INFO - */
    private final long turtle;
    private final long conversation;
    private final byte type;
    /* ---  ---  --- */

    private final @NotNull Direction  direction;
    private final @NotNull Connection connection;

    public CompiledPacket(final byte[] bytes, @NotNull Direction direction, @NotNull Connection connection) throws IllegalArgumentException {
        if (bytes.length < META_BYTES)
            throw new IllegalArgumentException("Missing packet information: " + bytes.length + " of " + META_BYTES + " bytes present.");

        this.bytes      = bytes;
        this.direction  = direction;
        this.connection = connection;

        this.turtle       = MathUtil.bytesToLong(bytes, 0);
        this.conversation = MathUtil.bytesToLong(bytes, Long.BYTES);
        this.type         = bytes[Long.BYTES * 2];
    }

    public CompiledPacket(final byte[] content, @NotNull Direction direction, @NotNull Connection connection, final long id, final long conversation, final byte type) {
        this(buildBytes(content, id, conversation, type), direction, connection);
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
        return this.doToPacket(this.bytes);
    }

    public @NotNull Packet toPacket(@NotNull String pass)
            throws IllegalStateException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        byte[] decrypted = Encryption.decrypt(this.bytes, pass);
        return this.doToPacket(decrypted);
    }

    private @NotNull Packet doToPacket(byte[] bytes) {
        Conversation conv = null; // TODO

        if (type == HandshakePacket.TYPE)
            return new HandshakePacket(turtle, conv, direction, bytes);

        if (type == DataPacket.TYPE)
            return new DataPacket(turtle, conv, direction, bytes);

        if (type == HeartbeatPacket.TYPE)
            return new HeartbeatPacket(turtle, conv, direction, bytes);

        if (type == ErrorPacket.TYPE)
            return new ErrorPacket(turtle, conv, bytes);

        throw new IllegalStateException("Illegal packet type: " + type);
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
