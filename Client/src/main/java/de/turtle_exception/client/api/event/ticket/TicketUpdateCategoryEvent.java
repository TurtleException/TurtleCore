package de.turtle_exception.client.api.event.ticket;

import de.turtle_exception.client.api.entities.Ticket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TicketUpdateCategoryEvent extends TicketEvent {
    protected final String oldCategory;
    protected final String newCategory;

    public TicketUpdateCategoryEvent(@NotNull Ticket ticket, @Nullable String oldCategory, @Nullable String newCategory) {
        super(ticket);
        this.oldCategory = oldCategory;
        this.newCategory = newCategory;
    }

    public @Nullable String getOldCategory() {
        return oldCategory;
    }

    public @Nullable String getNewCategory() {
        return newCategory;
    }
}
