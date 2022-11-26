package de.turtle_exception.client.api.event.entities.ticket;

import de.turtle_exception.client.api.entities.Ticket;
import de.turtle_exception.client.api.event.entities.EntityDeleteEvent;
import org.jetbrains.annotations.NotNull;

public class TicketDeleteEvent extends TicketEvent implements EntityDeleteEvent<Ticket> {
    public TicketDeleteEvent(@NotNull Ticket ticket) {
        super(ticket);
    }
}
