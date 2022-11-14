package de.turtle_exception.client.internal.net.packets;

import de.turtle_exception.client.internal.net.Connection;
import de.turtle_exception.client.internal.net.Direction;
import de.turtle_exception.client.internal.util.crypto.Encryption;
import de.turtle_exception.client.internal.util.time.TurtleType;
import de.turtle_exception.client.internal.util.time.TurtleUtil;
import org.jetbrains.annotations.NotNull;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public abstract class Packet {
    protected final byte type;
    protected final long id;

    protected final long deadline;
    protected final long responseCode;
    protected final @NotNull Connection connection;
    protected final @NotNull Direction  direction;

    protected Packet(long id, long deadline, @NotNull Connection connection, long responseCode, @NotNull Direction direction, byte type) {
        this.type = type;
        this.id = id;

        this.deadline     = deadline;
        this.responseCode = responseCode;
        this.connection   = connection;
        this.direction    = direction;
    }

    protected Packet(long deadline, @NotNull Connection connection, long responseCode, @NotNull Direction direction, byte type) {
        this(TurtleUtil.newId(TurtleType.PACKET), deadline, connection, responseCode, direction, type);
    }

    public final @NotNull CompiledPacket compile() {
        return this.doCompile(this.getBytes());
    }

    public final @NotNull CompiledPacket compile(@NotNull String pass)
            throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        return this.doCompile(Encryption.encrypt(this.getBytes(), pass));
    }

    private @NotNull CompiledPacket doCompile(byte[] bytes) {
        return new CompiledPacket(bytes, direction, connection, deadline, id, responseCode, type);
    }

    public abstract byte[] getBytes();

    public byte getType() {
        return type;
    }

    public long getId() {
        return id;
    }

    public long getDeadline() {
        return deadline;
    }

    public long getResponseCode() {
        return responseCode;
    }

    public @NotNull Connection getConnection() {
        return connection;
    }

    public @NotNull Direction getDirection() {
        return direction;
    }
}
