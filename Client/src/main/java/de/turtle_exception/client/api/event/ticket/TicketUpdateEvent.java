package de.turtle_exception.client.api.event.ticket;

import de.turtle_exception.client.api.entities.Ticket;
import org.jetbrains.annotations.NotNull;

public class TicketUpdateEvent<T> extends TicketEvent {
    private final @NotNull String key;

    protected final T oldValue;
    protected final T newValue;

    public TicketUpdateEvent(@NotNull Ticket ticket, @NotNull String key, T oldValue, T newValue) {
        super(ticket);
        this.key = key;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public final @NotNull String getKey() {
        return key;
    }

    public T getOldValue() {
        return oldValue;
    }

    public T getNewValue() {
        return newValue;
    }
}
