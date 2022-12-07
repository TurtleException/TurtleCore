package de.turtle_exception.client.api.event.entities.ticket;

import de.turtle_exception.client.api.entities.Ticket;
import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.event.entities.EntityUpdateEntryEvent;
import de.turtle_exception.client.internal.data.annotations.Keys;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Function;

public class TicketUserRemoveEvent extends TicketEvent implements EntityUpdateEntryEvent<Ticket, Long> {
    protected final long user;

    public TicketUserRemoveEvent(@NotNull Ticket ticket, long user) {
        super(ticket);
        this.user = user;
    }

    public long getUserId() {
        return user;
    }

    /* - EntityUpdateEntryEvent - */

    @Override
    public final @NotNull String getKey() {
        return Keys.Ticket.USERS;
    }

    @Override
    public final @NotNull Collection<Long> getCollection() {
        return getTicket().getUserIds();
    }

    @Override
    public final @NotNull Function<Long, Object> getMutator() {
        return l -> l;
    }
}
