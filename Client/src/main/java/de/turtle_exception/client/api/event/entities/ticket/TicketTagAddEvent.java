package de.turtle_exception.client.api.event.entities.ticket;

import de.turtle_exception.client.api.entities.Ticket;
import de.turtle_exception.client.api.event.entities.EntityUpdateEntryEvent;
import de.turtle_exception.client.internal.data.annotations.Keys;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Function;

@SuppressWarnings("unused")
public class TicketTagAddEvent extends TicketEvent implements EntityUpdateEntryEvent<Ticket, String> {
    protected final String tag;

    public TicketTagAddEvent(@NotNull Ticket ticket, @NotNull String tag) {
        super(ticket);
        this.tag = tag;
    }

    public @NotNull String getTag() {
        return tag;
    }

    /* - EntityUpdateEntryEvent - */

    @Override
    public final @NotNull String getKey() {
        return Keys.Ticket.TAGS;
    }

    @Override
    public final @NotNull Collection<String> getCollection() {
        return getTicket().getTags();
    }

    @Override
    public final @NotNull Function<String, Object> getMutator() {
        return s -> s;
    }
}
