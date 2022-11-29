package de.turtle_exception.client.api.entities.containers.messages;

import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.api.entities.containers.ITurtleContainer;
import de.turtle_exception.client.api.entities.messages.SyncChannel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/** Represents an object that can cache {@link SyncChannel SyncChannels}. */
public interface IChannelContainer extends ITurtleContainer {
    /**
     * Returns an immutable List of all cached {@link SyncChannel} objects.
     * @return List of cached SyncChannels.
     */
    @NotNull List<SyncChannel> getChannels();

    /**
     * Returns a single {@link SyncChannel} specified by its id, or {@code null} if no such object is stored in the
     * underlying cache.
     * @param id The unique id of the SyncChannel.
     * @return The requested SyncChannel (may be {@code null}).
     * @see SyncChannel#getId()
     */
    @Nullable SyncChannel getChannelById(long id);

    @Override
    default @NotNull List<Turtle> getTurtles() {
        return List.copyOf(new ArrayList<>(getChannels()));
    }

    @Override
    default @Nullable Turtle getTurtleById(long id) {
        return getChannelById(id);
    }
}
