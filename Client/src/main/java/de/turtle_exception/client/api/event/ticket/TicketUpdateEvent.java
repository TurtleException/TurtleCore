package de.turtle_exception.client.api.event.ticket;

import de.turtle_exception.client.api.entities.Ticket;
import org.jetbrains.annotations.NotNull;

public class TicketUpdateEvent<T> extends TicketEvent {
    protected final T oldValue;
    protected final T newValue;

    public TicketUpdateEvent(@NotNull Ticket ticket, T oldValue, T newValue) {
        super(ticket);
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public T getOldValue() {
        return oldValue;
    }

    public T getNewValue() {
        return newValue;
    }
}
