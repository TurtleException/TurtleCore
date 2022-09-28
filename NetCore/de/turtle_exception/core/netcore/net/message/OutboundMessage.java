package de.turtle_exception.core.netcore.net.message;

import de.turtle_exception.core.netcore.TurtleCore;
import de.turtle_exception.core.netcore.net.route.CompiledRoute;
import org.jetbrains.annotations.NotNull;

public class OutboundMessage extends Message {
    public OutboundMessage(@NotNull TurtleCore core, @NotNull CompiledRoute route, long deadline) {
        super(core, route, deadline);
    }

    public void handleResponse(@NotNull InboundMessage response) {
        // TODO: ?
    }
}
