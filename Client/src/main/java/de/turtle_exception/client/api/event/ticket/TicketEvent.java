package de.turtle_exception.client.api.event.ticket;

import de.turtle_exception.client.api.entities.Ticket;
import de.turtle_exception.client.api.event.Event;
import org.jetbrains.annotations.NotNull;

public abstract class TicketEvent extends Event {
    protected final Ticket ticket;

    public TicketEvent(@NotNull Ticket ticket) {
        super(ticket.getClient());
        this.ticket = ticket;
    }

    public @NotNull Ticket getTicket() {
        return ticket;
    }
}
