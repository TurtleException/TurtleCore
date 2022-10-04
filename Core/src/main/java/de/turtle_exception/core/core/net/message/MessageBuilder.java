package de.turtle_exception.core.core.net.message;

import de.turtle_exception.core.core.TurtleCore;
import de.turtle_exception.core.core.net.NetworkAdapter;
import de.turtle_exception.core.core.net.route.CompiledRoute;
import de.turtle_exception.core.core.util.Checks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MessageBuilder {
    private @Nullable TurtleCore core;
    private @Nullable NetworkAdapter netAdapter;
    private @Nullable CompiledRoute route;
    private @Nullable Long timeout;
    private @Nullable Long conversation;

    public MessageBuilder() { }

    public @NotNull InboundMessage build() throws IllegalArgumentException {
        try {
            Checks.nonNull(core, "Core");
            Checks.nonNull(netAdapter, "NetworkAdapter");
            Checks.nonNull(route, "Route");
            Checks.nonNull(timeout, "Timeout");
        } catch (NullPointerException e) {
            throw new IllegalArgumentException(e);
        }
        final long deadline = System.currentTimeMillis() + timeout;
        return new InboundMessage(core, netAdapter, conversation, route, deadline);
    }

    /* - - - */

    public MessageBuilder setCore(TurtleCore core) {
        this.core = core;
        return this;
    }

    public MessageBuilder setNetAdapter(NetworkAdapter netAdapter) {
        this.netAdapter = netAdapter;
        return this;
    }

    public MessageBuilder setRoute(CompiledRoute route) {
        this.route = route;
        return this;
    }

    public MessageBuilder setTimeout(Long timeout) {
        this.timeout = timeout;
        return this;
    }
}
