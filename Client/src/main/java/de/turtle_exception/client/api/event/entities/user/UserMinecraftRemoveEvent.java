package de.turtle_exception.client.api.event.entities.user;

import de.turtle_exception.client.api.entities.User;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@SuppressWarnings("unused")
public class UserMinecraftRemoveEvent extends UserEvent {
    protected final UUID uuid;

    public UserMinecraftRemoveEvent(@NotNull User user, @NotNull UUID uuid) {
        super(user);
        this.uuid = uuid;
    }

    public @NotNull UUID getUUID() {
        return uuid;
    }
}
