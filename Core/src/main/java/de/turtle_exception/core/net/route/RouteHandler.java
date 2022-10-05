package de.turtle_exception.core.net.route;

import de.turtle_exception.core.net.NetworkAdapter;
import de.turtle_exception.core.net.message.InboundMessage;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface RouteHandler {
    void handle(@NotNull NetworkAdapter netAdapter, @NotNull InboundMessage msg) throws Exception;
}
