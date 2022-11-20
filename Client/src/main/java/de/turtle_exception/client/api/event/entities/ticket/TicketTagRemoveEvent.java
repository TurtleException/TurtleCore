package de.turtle_exception.client.api.event.entities.ticket;

import de.turtle_exception.client.api.entities.Ticket;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class TicketTagRemoveEvent extends TicketEvent {
    protected final String tag;

    public TicketTagRemoveEvent(@NotNull Ticket ticket, @NotNull String tag) {
        super(ticket);
        this.tag = tag;
    }

    public @NotNull String getTag() {
        return tag;
    }
}