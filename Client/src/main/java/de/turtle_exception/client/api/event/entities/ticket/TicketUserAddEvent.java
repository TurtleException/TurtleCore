package de.turtle_exception.client.api.event.entities.ticket;

import de.turtle_exception.client.api.entities.Ticket;
import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.event.entities.EntityUpdateEntryEvent;
import de.turtle_exception.client.internal.data.annotations.Keys;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Function;

public class TicketUserAddEvent extends TicketEvent implements EntityUpdateEntryEvent<Ticket, User> {
    protected final User user;

    public TicketUserAddEvent(@NotNull Ticket ticket, @NotNull User user) {
        super(ticket);
        this.user = user;
    }

    public @NotNull User getUser() {
        return user;
    }

    /* - EntityUpdateEntryEvent - */

    @Override
    public final @NotNull String getKey() {
        return Keys.Ticket.USERS;
    }

    @Override
    public final @NotNull Collection<User> getCollection() {
        return getTicket().getUsers();
    }

    @Override
    public final @NotNull Function<User, Object> getMutator() {
        return Turtle::getId;
    }
}
