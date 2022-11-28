package de.turtle_exception.client.internal.request;

import de.turtle_exception.client.internal.data.Data;
import de.turtle_exception.client.internal.net.Connection;
import de.turtle_exception.client.internal.net.Direction;
import de.turtle_exception.client.internal.net.packets.DataPacket;
import de.turtle_exception.client.internal.util.time.TurtleType;
import de.turtle_exception.client.internal.util.time.TurtleUtil;
import org.jetbrains.annotations.NotNull;

public class Request {
    private final @NotNull DataPacket packet;

    public Request(@NotNull DataPacket packet) throws IllegalArgumentException {
        if (packet.getDirection() != Direction.OUTBOUND)
            throw new IllegalArgumentException("Request may only be outbound");

        this.packet = packet;
    }

    public Request(@NotNull Connection connection, @NotNull Data data) {
        this.packet = new DataPacket(
                System.currentTimeMillis() + connection.getAdapter().getClient().getTimeoutOutbound(),
                connection,
                TurtleUtil.newId(TurtleType.RESPONSE_CODE),
                data
        );
    }

    public @NotNull DataPacket getPacket() {
        return this.packet;
    }

    public @NotNull Connection getConnection() {
        return this.packet.getConnection();
    }
}
