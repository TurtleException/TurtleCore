package de.turtle_exception.client.internal.net.packets;

import de.turtle_exception.client.internal.net.Direction;
import de.turtle_exception.client.internal.net.message.Conversation;
import org.jetbrains.annotations.NotNull;

public abstract class Packet {
    protected final long id;

    protected final @NotNull Conversation conversation;
    protected final @NotNull Direction direction;

    protected Packet(long id, @NotNull Conversation conversation, @NotNull Direction direction) {
        this.id = id;
        this.conversation = conversation;
        this.direction = direction;
    }

    public final @NotNull CompiledPacket compile() {
        return new CompiledPacket(this.getBytes(), direction, conversation.getConnection());
    }

    public abstract byte[] getBytes();
}
