package de.turtle_exception.client.internal.net.message;

import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.internal.net.NetworkAdapter;
import de.turtle_exception.client.internal.net.route.CompiledRoute;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.function.Consumer;

public class InboundMessage extends Message {
    protected final @NotNull NetworkAdapter networkAdapter;

    public InboundMessage(@NotNull TurtleClient client, @NotNull NetworkAdapter networkAdapter, long conversation, @NotNull CompiledRoute route, long deadline) {
        super(client, conversation, route, deadline);
        this.networkAdapter = networkAdapter;
    }

    public void respond(@NotNull OutboundMessage message) {
        this.networkAdapter.submit(message);
    }

    public void respond(@NotNull CompiledRoute route) {
        this.respond(route, client.getDefaultTimeoutOutbound(), in -> { });
    }

    public void respond(@NotNull CompiledRoute route, @Range(from = 0, to = Long.MAX_VALUE) long timeout, @NotNull Consumer<InboundMessage> handler) {
        this.respond(new OutboundMessage(this.client, this.conversation, route, System.currentTimeMillis() + timeout, handler));
    }
}
