package de.turtle_exception.client.api.event.entities.user;

import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.event.entities.EntityUpdateEntryEvent;
import de.turtle_exception.client.internal.data.annotations.Keys;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.UUID;
import java.util.function.Function;

@SuppressWarnings("unused")
public class UserMinecraftRemoveEvent extends UserEvent implements EntityUpdateEntryEvent<User, UUID> {
    protected final UUID uuid;

    public UserMinecraftRemoveEvent(@NotNull User user, @NotNull UUID uuid) {
        super(user);
        this.uuid = uuid;
    }

    public @NotNull UUID getUUID() {
        return uuid;
    }

    /* - EntityUpdateEntryEvent - */

    @Override
    public final @NotNull String getKey() {
        return Keys.User.MINECRAFT;
    }

    @Override
    public final @NotNull Collection<UUID> getCollection() {
        return getUser().getMinecraftIds();
    }

    @Override
    public final @NotNull Function<UUID, Object> getMutator() {
        return UUID::toString;
    }
}
