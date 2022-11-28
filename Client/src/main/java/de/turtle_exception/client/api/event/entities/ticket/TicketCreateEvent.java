package de.turtle_exception.client.api.event.entities.ticket;

import de.turtle_exception.client.api.entities.Ticket;
import de.turtle_exception.client.api.event.entities.EntityCreateEvent;
import org.jetbrains.annotations.NotNull;

public class TicketCreateEvent extends TicketEvent implements EntityCreateEvent<Ticket> {
    public TicketCreateEvent(@NotNull Ticket ticket) {
        super(ticket);
    }
}
