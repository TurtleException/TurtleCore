package de.turtle_exception.client.internal.net.message;

import de.turtle_exception.client.internal.net.route.CompiledRoute;
import de.turtle_exception.core.TurtleCore;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class OutboundMessage extends Message {
    private final Consumer<InboundMessage> handler;

    public OutboundMessage(@NotNull TurtleCore core, long conversation, @NotNull CompiledRoute route, long deadline, @NotNull Consumer<InboundMessage> handler) {
        super(core, conversation, route, deadline);
        this.handler = handler;
    }

    public void handleResponse(@NotNull InboundMessage response) {
        this.done = true;
        this.handler.accept(response);
    }
}
