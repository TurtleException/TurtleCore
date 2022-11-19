package de.turtle_exception.client.api.event.entities.ticket;

import de.turtle_exception.client.api.entities.Ticket;
import de.turtle_exception.client.api.entities.User;
import org.jetbrains.annotations.NotNull;

public class TicketUserAddEvent extends TicketEvent {
    protected final User user;

    public TicketUserAddEvent(@NotNull Ticket ticket, @NotNull User user) {
        super(ticket);
        this.user = user;
    }

    public @NotNull User getUser() {
        return user;
    }
}
