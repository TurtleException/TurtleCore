package de.turtle_exception.client.internal.net.packets;

import de.turtle_exception.client.internal.net.Direction;
import de.turtle_exception.client.internal.net.message.Conversation;
import org.jetbrains.annotations.NotNull;

public class HandshakePacket extends Packet {
    public static final byte TYPE = 0;

    public HandshakePacket(long id, @NotNull Conversation conversation, @NotNull Direction direction, byte[] bytes) {
        super(id, conversation, direction, TYPE);
    }

    @Override
    public byte[] getBytes() {
        return new byte[0];
    }
}
