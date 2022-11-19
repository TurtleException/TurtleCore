package de.turtle_exception.client.api.event.ticket;

import de.turtle_exception.client.api.entities.Ticket;
import org.jetbrains.annotations.NotNull;

public class TicketUpdateDiscordChannelEvent extends TicketUpdateEvent<Long> {
    public TicketUpdateDiscordChannelEvent(@NotNull Ticket ticket, long oldDiscordChannelId, long newDiscordChannelId) {
        super(ticket, "discord_channel", oldDiscordChannelId, newDiscordChannelId);
    }
}
