package de.turtle_exception.client.api.entities.containers.messages;

import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.api.entities.containers.ITurtleContainer;
import de.turtle_exception.client.api.entities.messages.MinecraftChannel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/** Represents an object that can cache {@link MinecraftChannel MinecraftChannels}. */
public interface IMinecraftChannelContainer extends ITurtleContainer {
    /**
     * Returns an immutable List of all cached {@link MinecraftChannel} objects.
     * @return List of cached MinecraftChannels.
     */
    @NotNull List<MinecraftChannel> getMinecraftChannels();

    /**
     * Returns a single {@link MinecraftChannel} specified by its id, or {@code null} if no such object is stored in the
     * underlying cache.
     * @param id The unique id of the MinecraftChannel.
     * @return The requested MinecraftChannel (may be {@code null}).
     * @see MinecraftChannel#getId()
     */
    @Nullable MinecraftChannel getMinecraftChannelById(long id);

    @Override
    default @NotNull List<Turtle> getTurtles() {
        return List.copyOf(new ArrayList<>(getMinecraftChannels()));
    }

    @Override
    default @Nullable Turtle getTurtleById(long id) {
        return getMinecraftChannelById(id);
    }
}
