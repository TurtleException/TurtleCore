package de.turtle_exception.core.netcore.net.route;

import de.turtle_exception.core.netcore.net.message.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

public class RouteManager {
    /** This map links all finalizers to their route. */
    private final ConcurrentHashMap<Route, BiConsumer<Message, Message>> registry = new ConcurrentHashMap<>();

    /** The default finalizer that will be used if no specific finalizer is registered for a route. */
    private final @NotNull BiConsumer<Message, Message> defaultFinalizer = (message, message2) -> { };

    public void setRouteFinalizer(@NotNull Route route, @Nullable BiConsumer<Message, Message> finalizer) {
        if (finalizer != null)
            registry.put(route, finalizer);
        else
            registry.remove(route);
    }

    public @NotNull BiConsumer<Message, Message> getRouteFinalizer(@NotNull Route route) {
        return registry.getOrDefault(route, defaultFinalizer);
    }
}
