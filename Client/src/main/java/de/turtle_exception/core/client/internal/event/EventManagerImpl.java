package de.turtle_exception.core.client.internal.event;

import com.google.common.collect.Sets;
import de.turtle_exception.core.client.api.EventManager;
import de.turtle_exception.core.client.api.Listener;
import de.turtle_exception.core.client.api.events.Event;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class EventManagerImpl implements EventManager {
    private final Set<Listener> listeners = Sets.newConcurrentHashSet();

    @Override
    public void handleEvent(@NotNull Event event) {
        for (Listener listener : listeners) {
            listener.onEvent(event);
        }
    }

    @Override
    public void register(@NotNull Listener listener) {
        listeners.add(listener);
    }

    @Override
    public void unregister(@NotNull Listener listener) {
        listeners.remove(listener);
    }
}
