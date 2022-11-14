package de.turtle_exception.client.internal.request;

import de.turtle_exception.client.internal.data.Data;
import de.turtle_exception.client.internal.net.Direction;
import de.turtle_exception.client.internal.net.packets.DataPacket;
import de.turtle_exception.client.internal.net.packets.Packet;
import org.jetbrains.annotations.NotNull;

public class Response implements IResponse {
    private final @NotNull DataPacket packet;

    public Response(@NotNull DataPacket packet) throws IllegalArgumentException {
        if (packet.getDirection() != Direction.INBOUND)
            throw new IllegalArgumentException("Response may only be inbound");

        this.packet = packet;
    }

    public @NotNull Packet getPacket() {
        return packet;
    }

    public @NotNull Data getData() {
        return packet.getData();
    }
}
