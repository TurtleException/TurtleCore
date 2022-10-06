package de.turtle_exception.client.api.event.user;

import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.User;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class UserDiscordRemoveEvent extends UserEvent {
    protected final long discordId;

    public UserDiscordRemoveEvent(@NotNull TurtleClient client, @NotNull User user, long discordId) {
        super(client, user);
        this.discordId = discordId;
    }

    public long getDiscordId() {
        return discordId;
    }
}
