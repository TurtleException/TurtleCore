package de.turtle_exception.client.api.event.entities.ticket;

import de.turtle_exception.client.api.entities.Ticket;
import org.jetbrains.annotations.NotNull;

public class TicketUpdateCategoryEvent extends TicketUpdateEvent<String> {
    public TicketUpdateCategoryEvent(@NotNull Ticket ticket, @NotNull String oldCategory, @NotNull String newCategory) {
        super(ticket, "category", oldCategory, newCategory);
    }
}
