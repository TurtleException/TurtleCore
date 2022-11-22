package de.turtle_exception.client.api.event.entities.ticket;

import de.turtle_exception.client.api.TicketState;
import de.turtle_exception.client.api.entities.Ticket;
import de.turtle_exception.client.internal.data.annotations.Keys;
import org.jetbrains.annotations.NotNull;

public class TicketUpdateStateEvent extends TicketUpdateEvent<TicketState> {
    public TicketUpdateStateEvent(@NotNull Ticket ticket, @NotNull TicketState oldState, @NotNull TicketState newState) {
        super(ticket, Keys.Ticket.STATE, oldState, newState);
    }
}
