package de.turtle_exception.core.server.net.handlers;

import de.turtle_exception.core.core.net.NetworkAdapter;
import de.turtle_exception.core.core.net.message.InboundMessage;
import de.turtle_exception.core.core.net.route.RouteErrors;
import de.turtle_exception.core.core.net.route.RouteHandler;
import de.turtle_exception.core.server.data.DataAccessException;
import de.turtle_exception.core.server.data.DataService;
import de.turtle_exception.core.server.net.VirtualClient;
import org.jetbrains.annotations.NotNull;

public abstract class ResourceHandler implements RouteHandler {
    protected final VirtualClient client;

    public ResourceHandler(@NotNull VirtualClient client) {
        this.client = client;
    }

    @Override
    public final void handle(@NotNull NetworkAdapter netAdapter, @NotNull InboundMessage msg) throws Exception {
        try {
            this.handle(msg);
        } catch (DataAccessException e) {
            msg.respond(RouteErrors.UNKNOWN.with(e).compile());
        }
    }

    public abstract void handle(@NotNull InboundMessage msg) throws Exception;

    // helper method
    protected DataService getDataService() {
        return client.getInternalServer().getServer().getDataService();
    }
}
