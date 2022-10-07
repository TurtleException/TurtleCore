package de.turtle_exception.client.api.event.ticket;

import de.turtle_exception.client.api.entities.Ticket;
import org.jetbrains.annotations.NotNull;

public class TicketUpdateDiscordChannelEvent extends TicketEvent {
    protected final long oldDiscordChannelId;
    protected final long newDiscordChannelId;

    public TicketUpdateDiscordChannelEvent(@NotNull Ticket ticket, long oldDiscordChannelId, long newDiscordChannelId) {
        super(ticket);
        this.oldDiscordChannelId = oldDiscordChannelId;
        this.newDiscordChannelId = newDiscordChannelId;
    }

    public long getOldDiscordChannelId() {
        return oldDiscordChannelId;
    }

    public long getNewDiscordChannelId() {
        return newDiscordChannelId;
    }
}
