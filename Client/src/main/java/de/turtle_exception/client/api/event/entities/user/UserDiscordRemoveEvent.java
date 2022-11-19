package de.turtle_exception.client.api.event.entities.user;

import de.turtle_exception.client.api.entities.User;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class UserDiscordRemoveEvent extends UserEvent {
    protected final long discordId;

    public UserDiscordRemoveEvent(@NotNull User user, long discordId) {
        super(user);
        this.discordId = discordId;
    }

    public long getDiscordId() {
        return discordId;
    }
}
