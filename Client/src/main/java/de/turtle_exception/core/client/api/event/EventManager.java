package de.turtle_exception.core.client.api.event;

import com.google.common.collect.Sets;
import de.turtle_exception.core.client.api.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class EventManager {
    private final Set<Listener> listeners = Sets.newConcurrentHashSet();

    public void handleEvent(@NotNull Event event) {
        for (Listener listener : listeners) {
            listener.onEvent(event);
        }
    }

    public void register(@NotNull Listener listener) {
        listeners.add(listener);
    }

    public void unregister(@NotNull Listener listener) {
        listeners.remove(listener);
    }
}
