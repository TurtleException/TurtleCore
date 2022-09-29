package de.turtle_exception.core.client.api.event;

import com.google.common.collect.Sets;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class EventManager {
    private final Set<EventListener> listeners = Sets.newConcurrentHashSet();

    public void handleEvent(@NotNull Event event) {
        for (EventListener listener : listeners) {
            listener.onGenericEvent(event);
        }
    }

    public void register(@NotNull EventListener listener) {
        listeners.add(listener);
    }

    public void unregister(@NotNull EventListener listener) {
        listeners.remove(listener);
    }
}
