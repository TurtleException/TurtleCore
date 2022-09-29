package de.turtle_exception.core.netcore.net.message;

import de.turtle_exception.core.netcore.TurtleCore;
import de.turtle_exception.core.netcore.net.NetworkAdapter;
import de.turtle_exception.core.netcore.net.route.CompiledRoute;
import org.jetbrains.annotations.NotNull;

public class InboundMessage extends Message {
    protected final @NotNull NetworkAdapter networkAdapter;

    public InboundMessage(@NotNull TurtleCore core, @NotNull NetworkAdapter networkAdapter, @NotNull CompiledRoute route, long deadline) {
        super(core, route, deadline);
        this.networkAdapter = networkAdapter;
    }

    public void respond(@NotNull OutboundMessage message) {
        this.networkAdapter.submit(message);
    }
}
