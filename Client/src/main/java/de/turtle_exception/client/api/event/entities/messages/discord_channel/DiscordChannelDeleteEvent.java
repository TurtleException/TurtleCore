package de.turtle_exception.client.api.event.entities.messages.discord_channel;

import de.turtle_exception.client.api.entities.messages.DiscordChannel;
import org.jetbrains.annotations.NotNull;

public class DiscordChannelDeleteEvent extends DiscordChannelEvent {
    public DiscordChannelDeleteEvent(@NotNull DiscordChannel discordChannel) {
        super(discordChannel);
    }
}
