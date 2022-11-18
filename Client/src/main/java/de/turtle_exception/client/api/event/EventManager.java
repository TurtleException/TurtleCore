package de.turtle_exception.client.api.event;

import com.google.common.collect.Sets;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.internal.util.logging.NestedLogger;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.logging.Level;

@SuppressWarnings("unused")
public class EventManager {
    private final TurtleClient client;
    private final NestedLogger logger;

    private final Set<EventListener> listeners = Sets.newConcurrentHashSet();

    public EventManager(@NotNull TurtleClient client) {
        this.client = client;
        this.logger = new NestedLogger("EventManager", client.getLogger());
    }

    public void handleEvent(@NotNull Event event) {
        this.logger.log(Level.FINE, "Passing " + event.getClass().getSimpleName() + " to " + listeners.size() + " listeners.");
        for (EventListener listener : listeners)
            listener.onGenericEvent(event);
    }

    public void register(@NotNull EventListener listener) {
        listeners.add(listener);
        this.logger.log(Level.FINE, "Registered new EventListener: " + listener.getClass().getName());
    }

    public void unregister(@NotNull EventListener listener) {
        listeners.remove(listener);
        this.logger.log(Level.FINE, "Unregistered EventListener: " + listener.getClass().getName());
    }

    /* - - - */

    public @NotNull TurtleClient getClient() {
        return client;
    }
}
