package de.turtle_exception.client.internal.net.message;

import de.turtle_exception.client.internal.net.NetworkAdapter;
import de.turtle_exception.client.internal.net.route.CompiledRoute;
import de.turtle_exception.core.TurtleCore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.function.Consumer;

public class InboundMessage extends Message {
    protected final @NotNull NetworkAdapter networkAdapter;

    public InboundMessage(@NotNull TurtleCore core, @NotNull NetworkAdapter networkAdapter, long conversation, @NotNull CompiledRoute route, long deadline) {
        super(core, conversation, route, deadline);
        this.networkAdapter = networkAdapter;
    }

    public void respond(@NotNull OutboundMessage message) {
        this.networkAdapter.submit(message);
    }

    public void respond(@NotNull CompiledRoute route) {
        this.respond(route, core.getDefaultTimeoutOutbound(), in -> { });
    }

    public void respond(@NotNull CompiledRoute route, @Range(from = 0, to = Long.MAX_VALUE) long timeout, @NotNull Consumer<InboundMessage> handler) {
        this.respond(new OutboundMessage(this.core, this.conversation, route, System.currentTimeMillis() + timeout, handler));
    }
}
