package de.turtle_exception.client.api.event.ticket;

import de.turtle_exception.client.api.TicketState;
import de.turtle_exception.client.api.entities.Ticket;
import org.jetbrains.annotations.NotNull;

public class TicketUpdateStateEvent extends TicketUpdateEvent<TicketState> {
    public TicketUpdateStateEvent(@NotNull Ticket ticket, @NotNull TicketState oldState, @NotNull TicketState newState) {
        super(ticket, "state", oldState, newState);
    }
}
