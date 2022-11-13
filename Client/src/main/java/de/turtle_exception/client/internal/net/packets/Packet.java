package de.turtle_exception.client.internal.net.packets;

import de.turtle_exception.client.internal.net.Direction;
import de.turtle_exception.client.internal.net.message.Conversation;
import de.turtle_exception.client.internal.util.crypto.Encryption;
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

    protected final @NotNull Conversation conversation;
    protected final @NotNull Direction direction;

    protected Packet(long id, @NotNull Conversation conversation, @NotNull Direction direction, byte type) {
        this.type = type;

        this.id = id;
        this.conversation = conversation;
        this.direction = direction;
    }

    public final @NotNull CompiledPacket compile() {
        return this.doCompile(this.getBytes());
    }

    public final @NotNull CompiledPacket compile(@NotNull String pass)
            throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        return this.doCompile(Encryption.encrypt(this.getBytes(), pass));
    }

    private @NotNull CompiledPacket doCompile(byte[] bytes) {
        return new CompiledPacket(bytes, direction, conversation.getConnection(), id, conversation.getId(), type);
    }

    public abstract byte[] getBytes();

    public byte getType() {
        return type;
    }

    public long getId() {
        return id;
    }

    public @NotNull Conversation getConversation() {
        return conversation;
    }

    public @NotNull Direction getDirection() {
        return direction;
    }
}
