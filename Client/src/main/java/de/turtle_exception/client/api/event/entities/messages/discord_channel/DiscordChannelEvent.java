package de.turtle_exception.client.api.event.entities.messages.discord_channel;

import de.turtle_exception.client.api.entities.messages.DiscordChannel;
import de.turtle_exception.client.api.event.entities.EntityEvent;
import org.jetbrains.annotations.NotNull;

public abstract class DiscordChannelEvent extends EntityEvent<DiscordChannel> {
    public DiscordChannelEvent(@NotNull DiscordChannel discordChannel) {
        super(discordChannel);
    }

    public @NotNull DiscordChannel getDiscordChannel() {
        return this.getEntity();
    }
}
