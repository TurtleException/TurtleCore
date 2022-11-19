package de.turtle_exception.client.api.event.entities.ticket;

import de.turtle_exception.client.api.entities.Ticket;
import de.turtle_exception.client.api.event.entities.EntityEvent;
import org.jetbrains.annotations.NotNull;

public abstract class TicketEvent extends EntityEvent<Ticket> {
    public TicketEvent(@NotNull Ticket entity) {
        super(entity);
    }

    public @NotNull Ticket getTicket() {
        return this.getEntity();
    }
}
