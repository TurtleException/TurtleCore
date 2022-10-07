package de.turtle_exception.client.api.entities.attribute;

import de.turtle_exception.client.api.entities.Ticket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ITicketContainer {
    @NotNull List<Ticket> getTickets();

    @Nullable Ticket getTicketById(long id);
}
