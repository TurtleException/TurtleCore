package de.turtle_exception.client.api.event.entities.ticket;

import de.turtle_exception.client.api.entities.Ticket;
import de.turtle_exception.client.internal.data.annotations.Keys;
import org.jetbrains.annotations.NotNull;

public class TicketUpdateDiscordChannelEvent extends TicketUpdateEvent<Long> {
    public TicketUpdateDiscordChannelEvent(@NotNull Ticket ticket, long oldDiscordChannelId, long newDiscordChannelId) {
        super(ticket, Keys.Ticket.DISCORD_CHANNEL, oldDiscordChannelId, newDiscordChannelId);
    }
}
