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

public record CompiledPacket(
        long turtle,
        long responseCode,
        byte type,
        byte[] content,
        @NotNull Direction direction,
        @NotNull Connection connection,
        long deadline
) {
    public static final int META_BYTES = 17;

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

    public int length() {
        return META_BYTES + content.length;
    }

    public long getTime() {
        return TurtleUtil.getTime(turtle);
    }
}
