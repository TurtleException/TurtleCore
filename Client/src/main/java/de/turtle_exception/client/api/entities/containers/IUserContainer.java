package de.turtle_exception.client.api.entities.containers;

import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.api.entities.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** Represents an object that can cache {@link User Users}. */
@SuppressWarnings("unused")
public interface IUserContainer extends ITurtleContainer {
    /**
     * Returns an immutable List of all cached {@link User} objects.
     * @return List of cached Users.
     */
    @NotNull List<User> getUsers();

    /**
     * Returns a single {@link User} specified by its id, or {@code null} if no such object is stored in the
     * underlying cache.
     * @param id The unique id of the User.
     * @return The requested User (may be {@code null}).
     * @see User#getId()
     */
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

    /**
     * Returns a single {@link User} specified by its exclusive link to the provided Discord user id, or {@code null} if
     * no such object is stored in the underlying cache.
     * @param snowflake A Discord snowflake id.
     * @return The requested User (may be {@code null}).
     */
    default @Nullable User getUserByDiscord(long snowflake) {
        for (User user : this.getUsers())
            if (user.getDiscordIds().contains(snowflake))
                return user;
        return null;
    }

    /**
     * Returns a single {@link User} specified by its exclusive link to the provided Minecraft Account UUID, or
     * {@code null} if no such object is stored in the underlying cache.
     * @param uuid A Minecraft Account {@link UUID}.
     * @return The requested User (may be {@code null}).
     */
    default @Nullable User getUserByMinecraft(@NotNull UUID uuid) {
        for (User user : this.getUsers())
            if (user.getMinecraftIds().contains(uuid))
                return user;
        return null;
    }
}
