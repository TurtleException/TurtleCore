package de.turtle_exception.core.core.net.message;

import de.turtle_exception.core.core.TurtleCore;
import de.turtle_exception.core.core.net.route.CompiledRoute;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class OutboundMessage extends Message {
    private final Consumer<InboundMessage> handler;

    public OutboundMessage(@NotNull TurtleCore core, @NotNull CompiledRoute route, long deadline, @NotNull Consumer<InboundMessage> handler) {
        super(core, route, deadline);
        this.handler = handler;
    }

    public void handleResponse(@NotNull InboundMessage response) {
        this.done = true;
        this.handler.accept(response);
    }
}
