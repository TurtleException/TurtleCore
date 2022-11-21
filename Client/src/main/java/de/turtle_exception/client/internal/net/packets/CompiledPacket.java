package de.turtle_exception.client.internal.net.packets;

import de.turtle_exception.client.internal.net.Connection;
import de.turtle_exception.client.internal.net.Direction;
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

    private final byte[] content;

    /* - META INFO - */
    private final long turtle;
    private final long responseCode;
    private final byte type;
    /* ---  ---  --- */

    private final @NotNull Direction  direction;
    private final @NotNull Connection connection;

    private final long deadline;

    public CompiledPacket(long turtle, long responseCode, byte type, final byte[] content, @NotNull Direction direction, @NotNull Connection connection, long deadline) throws IllegalArgumentException {
        this.turtle       = turtle;
        this.responseCode = responseCode;
        this.type         = type;
        this.content      = content;
        this.direction    = direction;
        this.connection   = connection;
        this.deadline     = deadline;
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

    public byte[] getContent() {
        return content;
    }

    public int getLength() {
        return META_BYTES + content.length;
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
