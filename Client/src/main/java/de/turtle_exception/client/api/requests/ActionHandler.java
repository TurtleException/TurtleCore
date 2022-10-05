package de.turtle_exception.client.api.requests;

import de.turtle_exception.core.net.message.InboundMessage;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ActionHandler<T> {
    T handle(@NotNull InboundMessage msg, @NotNull Request<T> request) throws Exception;
}
