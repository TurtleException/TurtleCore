package de.turtle_exception.client.internal.net;

import de.turtle_exception.client.internal.net.packets.CompiledPacket;
import org.jetbrains.annotations.NotNull;

public class Request {
    private final @NotNull CompiledPacket packet;

    public Request(@NotNull CompiledPacket packet) throws IllegalArgumentException {
        if (packet.getDirection() != Direction.OUTBOUND)
            throw new IllegalArgumentException("Request may only be outbound");

        this.packet = packet;
    }

    public @NotNull CompiledPacket getPacket() {
        return this.packet;
    }

    public @NotNull Connection getConnection() {
        return this.packet.getConnection();
    }
}
