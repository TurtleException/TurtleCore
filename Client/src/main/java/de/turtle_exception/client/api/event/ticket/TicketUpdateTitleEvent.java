package de.turtle_exception.client.api.event.ticket;

import de.turtle_exception.client.api.entities.Ticket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class TicketUpdateTitleEvent extends TicketUpdateEvent<String> {
    public TicketUpdateTitleEvent(@NotNull Ticket ticket, @Nullable String oldName, @Nullable String newName) {
        super(ticket, "title", oldName, newName);
    }
}
