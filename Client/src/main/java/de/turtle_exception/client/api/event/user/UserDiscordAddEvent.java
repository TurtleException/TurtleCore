package de.turtle_exception.client.api.event.user;

import de.turtle_exception.client.api.entities.User;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class UserDiscordAddEvent extends UserEvent {
    protected final long discordId;

    public UserDiscordAddEvent(@NotNull User user, long discordId) {
        super(user);
        this.discordId = discordId;
    }

    public long getDiscordId() {
        return discordId;
    }
}
