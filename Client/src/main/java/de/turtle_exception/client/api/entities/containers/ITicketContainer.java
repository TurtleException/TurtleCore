package de.turtle_exception.client.api.entities.containers;

import de.turtle_exception.client.api.entities.Ticket;
import de.turtle_exception.client.api.entities.Turtle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/** Represents an object that can cache {@link Ticket Tickets}. */
public interface ITicketContainer extends ITurtleContainer {
    /**
     * Returns an immutable List of all cached {@link Ticket} objects.
     * @return List of cached Tickets.
     */
    @NotNull List<Ticket> getTickets();

    /**
     * Returns a single {@link Ticket} specified by its id, or {@code null} if no such object is stored in the
     * underlying cache.
     * @param id The unique id of the Ticket.
     * @return The requested Ticket (may be {@code null}).
     * @see Ticket#getId()
     */
    @Nullable Ticket getTicketById(long id);

    @Override
    default @NotNull List<Turtle> getTurtles() {
        return List.copyOf(new ArrayList<>(getTickets()));
    }

    @Override
    default @Nullable Turtle getTurtleById(long id) {
        return getTicketById(id);
    }
}
