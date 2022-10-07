package de.turtle_exception.client.api.event.ticket;

import de.turtle_exception.client.api.entities.Ticket;
import org.jetbrains.annotations.NotNull;

public class TicketUpdateStateEvent extends TicketUpdateEvent<Byte> {
    public TicketUpdateStateEvent(@NotNull Ticket ticket, byte oldState, byte newState) {
        super(ticket, oldState, newState);
    }
}
