package de.turtle_exception.client.api.event.user;

import de.turtle_exception.client.api.entities.User;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@SuppressWarnings("unused")
public class UserMinecraftAddEvent extends UserEvent {
    protected final UUID uuid;

    public UserMinecraftAddEvent(@NotNull User user, @NotNull UUID uuid) {
        super(user);
        this.uuid = uuid;
    }

    public @NotNull UUID getUUID() {
        return uuid;
    }
}
