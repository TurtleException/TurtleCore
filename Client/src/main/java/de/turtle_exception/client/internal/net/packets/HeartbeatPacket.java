package de.turtle_exception.client.internal.net.packets;

import de.turtle_exception.client.internal.net.Direction;
import de.turtle_exception.client.internal.net.message.Conversation;
import org.jetbrains.annotations.NotNull;

public class HeartbeatPacket extends Packet {
    public static final byte TYPE = 2;

    public HeartbeatPacket(long id, @NotNull Conversation conversation, @NotNull Direction direction) {
        super(id, conversation, direction, TYPE);
    }

    public HeartbeatPacket(long id, @NotNull Conversation conversation, @NotNull Direction  direction, byte[] bytes) {
        // TODO
    }

    @Override
    public byte[] getBytes() {
        return new byte[0];
    }
}
