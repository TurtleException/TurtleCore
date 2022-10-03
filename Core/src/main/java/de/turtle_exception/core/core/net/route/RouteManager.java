package de.turtle_exception.core.core.net.route;

import de.turtle_exception.core.core.net.message.InboundMessage;
import de.turtle_exception.core.core.net.message.OutboundMessage;
import de.turtle_exception.core.core.util.Checks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class RouteManager {
    /** This map links all finalizers to their route. */
    private final ConcurrentHashMap<String, Consumer<InboundMessage>> registry = new ConcurrentHashMap<>();

    private Consumer<LogRecord> log = logRecord -> { };

    private final Consumer<InboundMessage> emptyFinalizer = inboundMessage -> { };

    /** The default finalizer that will be used if no specific finalizer is registered for a route. */
    private final @NotNull Consumer<InboundMessage> defaultFinalizer = message -> {
        message.respond(new OutboundMessage(
                message.getCore(),
                Routes.ERROR
                        .setCallbackCode(message.getRoute().callbackCode())
                        .setContent(Errors.NOT_SUPPORTED)
                        .build(),
                System.currentTimeMillis() + message.getCore().getDefaultTimeoutOutbound(),
                inboundMessage -> { }
        ));
        log.accept(new LogRecord(Level.WARNING, "Inbound message on unsupported route: " + message));
    };


    public RouteManager() {
        this.setLog(null);
    }

    public void setRouteFinalizer(@NotNull Route route, @Nullable Consumer<InboundMessage> finalizer) {
        if (finalizer != null)
            registry.put(route.getCommand(), finalizer);
        else
            registry.remove(route.getCommand());
    }

    public @NotNull Consumer<InboundMessage> getRouteFinalizer(@NotNull String command) {
        return registry.getOrDefault(command, defaultFinalizer);
    }

    public void setEmptyFinalizer(@NotNull Route... routes) {
        for (Route route : routes)
            this.setRouteFinalizer(route, emptyFinalizer);
    }

    public void setLog(@Nullable Consumer<LogRecord> log) {
        this.log = Checks.nullOr(log, record -> { });
    }
}
