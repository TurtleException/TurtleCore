package de.turtle_exception.client.internal.net.packets;

import de.turtle_exception.client.internal.net.Direction;
import de.turtle_exception.client.internal.net.message.Conversation;
import org.jetbrains.annotations.NotNull;

public class HandshakePacket extends Packet {
    public static final byte TYPE = 0;

    protected final @NotNull String msg;

    public HandshakePacket(long id, @NotNull Conversation conversation, @NotNull Direction direction, byte[] bytes) {
        this(id, conversation, direction, new String(bytes));
    }

    public HandshakePacket(long id, @NotNull Conversation conversation, @NotNull Direction direction, @NotNull String msg) {
        super(id, conversation, direction, TYPE);
        this.msg = msg;
    }

    @Override
    public byte[] getBytes() {
        return msg.getBytes();
    }

    public @NotNull String getMessage() {
        return msg;
    }
}
