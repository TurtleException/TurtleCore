package de.turtle_exception.client.api.event.ticket;

import de.turtle_exception.client.api.entities.Ticket;
import org.jetbrains.annotations.NotNull;

public class TicketUpdateStateEvent extends TicketEvent {
    protected final byte oldState;
    protected final byte newState;

    public TicketUpdateStateEvent(@NotNull Ticket ticket, byte oldState, byte newState) {
        super(ticket);
        this.oldState = oldState;
        this.newState = newState;
    }

    public byte getOldState() {
        return oldState;
    }

    public byte getNewState() {
        return newState;
    }
}
