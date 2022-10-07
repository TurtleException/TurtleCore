package de.turtle_exception.client.api.event.ticket;

import de.turtle_exception.client.api.entities.Ticket;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class TicketTagAddEvent extends TicketEvent {
    protected final String tag;

    public TicketTagAddEvent(@NotNull Ticket ticket, @NotNull String tag) {
        super(ticket);
        this.tag = tag;
    }

    public @NotNull String getTag() {
        return tag;
    }
}
