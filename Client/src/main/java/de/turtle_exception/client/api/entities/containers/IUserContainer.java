package de.turtle_exception.client.api.entities.containers;

import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.api.entities.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
public interface IUserContainer extends ITurtleContainer {
    @NotNull List<User> getUsers();

    @Nullable User getUserById(long id);

    @Override
    default @NotNull List<Turtle> getTurtles() {
        return List.copyOf(new ArrayList<>(getUsers()));
    }

    @Override
    default @Nullable Turtle getTurtleById(long id) {
        return getUserById(id);
    }

    /* - DISCORD & MINECRAFT - */

    default @Nullable User getUserByDiscord(long snowflake) {
        for (User user : this.getUsers())
            if (user.getDiscordIds().contains(snowflake))
                return user;
        return null;
    }

    default @Nullable User getUserByMinecraft(@NotNull UUID uuid) {
        for (User user : this.getUsers())
            if (user.getMinecraftIds().contains(uuid))
                return user;
        return null;
    }
}
