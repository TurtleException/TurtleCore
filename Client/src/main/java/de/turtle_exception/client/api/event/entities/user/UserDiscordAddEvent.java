package de.turtle_exception.client.api.event.entities.user;

import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.event.entities.EntityUpdateEntryEvent;
import de.turtle_exception.client.internal.data.annotations.Keys;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Function;

@SuppressWarnings("unused")
public class UserDiscordAddEvent extends UserEvent implements EntityUpdateEntryEvent<User, Long> {
    protected final long discordId;

    public UserDiscordAddEvent(@NotNull User user, long discordId) {
        super(user);
        this.discordId = discordId;
    }

    public long getDiscordId() {
        return discordId;
    }

    /* - EntityUpdateEntryEvent - */

    @Override
    public final @NotNull String getKey() {
        return Keys.User.DISCORD;
    }

    @Override
    public final @NotNull Collection<Long> getCollection() {
        return getUser().getDiscordIds();
    }

    @Override
    public final @NotNull Function<Long, Object> getMutator() {
        return l -> l;
    }
}
