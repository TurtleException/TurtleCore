package de.turtle_exception.client.internal.net;

import de.turtle_exception.client.internal.net.packets.Packet;
import org.jetbrains.annotations.NotNull;

public class Response {
    private final @NotNull Packet packet;

    public Response(@NotNull Packet packet) throws IllegalArgumentException {
        if (packet.getDirection() != Direction.INBOUND)
            throw new IllegalArgumentException("Response may only be inbound");

        this.packet = packet;
    }

    public @NotNull Packet getPacket() {
        return packet;
    }

    public @NotNull Conversation getConversation() {
        return packet.getConversation();
    }
}
