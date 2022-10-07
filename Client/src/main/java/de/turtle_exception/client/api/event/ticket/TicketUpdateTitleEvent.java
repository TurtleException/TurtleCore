package de.turtle_exception.client.api.event.ticket;

import de.turtle_exception.client.api.entities.Ticket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TicketUpdateTitleEvent extends TicketEvent {
    protected final String oldName;
    protected final String newName;

    public TicketUpdateTitleEvent(@NotNull Ticket ticket, @Nullable String oldName, @Nullable String newName) {
        super(ticket);
        this.oldName = oldName;
        this.newName = newName;
    }

    public @Nullable String getOldName() {
        return oldName;
    }

    public @Nullable String getNewName() {
        return newName;
    }
}
