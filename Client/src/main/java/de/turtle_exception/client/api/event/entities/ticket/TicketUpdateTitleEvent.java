package de.turtle_exception.client.api.event.entities.ticket;

import de.turtle_exception.client.api.entities.Ticket;
import de.turtle_exception.client.internal.data.annotations.Keys;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class TicketUpdateTitleEvent extends TicketUpdateEvent<String> {
    public TicketUpdateTitleEvent(@NotNull Ticket ticket, @Nullable String oldName, @Nullable String newName) {
        super(ticket, Keys.Ticket.TITLE, oldName, newName);
    }
}
