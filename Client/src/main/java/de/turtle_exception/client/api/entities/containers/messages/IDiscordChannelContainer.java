package de.turtle_exception.client.api.entities.containers.messages;

import de.turtle_exception.client.api.entities.messages.DiscordChannel;
import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.api.entities.containers.ITurtleContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/** Represents an object that can cache {@link DiscordChannel DiscordChannels}. */
public interface IDiscordChannelContainer extends ITurtleContainer {
    /**
     * Returns an immutable List of all cached {@link DiscordChannel} objects.
     * @return List of cached DiscordChannels.
     */
    @NotNull List<DiscordChannel> getDiscordChannels();

    /**
     * Returns a single {@link DiscordChannel} specified by its id, or {@code null} if no such object is stored in the
     * underlying cache.
     * @param id The unique id of the DiscordChannel.
     * @return The requested DiscordChannel (may be {@code null}).
     * @see DiscordChannel#getId()
     */
    @Nullable DiscordChannel getDiscordChannelById(long id);

    @Override
    default @NotNull List<Turtle> getTurtles() {
        return List.copyOf(new ArrayList<>(getDiscordChannels()));
    }

    @Override
    default @Nullable Turtle getTurtleById(long id) {
        return getDiscordChannelById(id);
    }
}
