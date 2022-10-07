package de.turtle_exception.client.api.event.ticket;

import de.turtle_exception.client.api.entities.Ticket;
import org.jetbrains.annotations.NotNull;

public class TicketCreateEvent extends TicketEvent {
    public TicketCreateEvent(@NotNull Ticket ticket) {
        super(ticket);
    }
}
