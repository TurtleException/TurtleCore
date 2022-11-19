package de.turtle_exception.server.event;

import de.turtle_exception.client.api.event.EventListener;
import de.turtle_exception.client.internal.data.Data;
import de.turtle_exception.client.internal.net.Connection;
import de.turtle_exception.client.internal.net.packets.DataPacket;
import de.turtle_exception.client.internal.util.time.TurtleType;
import de.turtle_exception.client.internal.util.time.TurtleUtil;
import de.turtle_exception.server.net.NetServer;
import org.jetbrains.annotations.NotNull;

abstract class AbstractListener extends EventListener {
    protected final @NotNull NetServer server;

    public AbstractListener(@NotNull NetServer server) {
        this.server = server;
    }

    protected final void sendPacket(@NotNull Data data) {
        final long deadline = System.currentTimeMillis() + server.getClient().getDefaultTimeoutOutbound();
        final long responseCode = TurtleUtil.newId(TurtleType.RESPONSE_CODE);

        for (Connection client : server.getClients())
            client.send(new DataPacket(deadline, client, responseCode, data), true);
    }
}
