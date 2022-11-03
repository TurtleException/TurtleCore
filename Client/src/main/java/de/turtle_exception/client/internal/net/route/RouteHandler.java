package de.turtle_exception.client.internal.net.route;

import de.turtle_exception.client.internal.net.NetworkAdapter;
import de.turtle_exception.client.internal.net.message.InboundMessage;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface RouteHandler {
    void handle(@NotNull NetworkAdapter netAdapter, @NotNull InboundMessage msg) throws Exception;
}
