package de.turtle_exception.client.api.event.entities.ticket;

import de.turtle_exception.client.api.entities.Ticket;
import de.turtle_exception.client.api.event.entities.EntityUpdateEvent;
import org.jetbrains.annotations.NotNull;

public class TicketUpdateEvent<V> extends TicketEvent implements EntityUpdateEvent<Ticket, V> {
    private final @NotNull String key;

    protected final V oldValue;
    protected final V newValue;

    public TicketUpdateEvent(@NotNull Ticket ticket, @NotNull String key, V oldValue, V newValue) {
        super(ticket);
        this.key = key;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    @Override
    public final @NotNull String getKey() {
        return key;
    }

    @Override
    public V getOldValue() {
        return oldValue;
    }

    @Override
    public V getNewValue() {
        return newValue;
    }
}
