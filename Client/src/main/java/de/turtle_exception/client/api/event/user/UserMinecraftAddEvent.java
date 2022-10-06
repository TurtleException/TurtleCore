package de.turtle_exception.client.api.event.user;

import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.User;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class UserMinecraftAddEvent extends UserEvent {
    protected final UUID uuid;

    public UserMinecraftAddEvent(@NotNull TurtleClient client, @NotNull User user, @NotNull UUID uuid) {
        super(client, user);
        this.uuid = uuid;
    }

    public @NotNull UUID getUUID() {
        return uuid;
    }
}
