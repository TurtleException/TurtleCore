package de.turtle_exception.client.api.event.entities.messages.discord_channel;

import de.turtle_exception.client.api.entities.messages.DiscordChannel;
import org.jetbrains.annotations.NotNull;

public class DiscordChannelCreateEvent extends DiscordChannelEvent {
    public DiscordChannelCreateEvent(@NotNull DiscordChannel discordChannel) {
        super(discordChannel);
    }
}
