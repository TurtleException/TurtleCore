package de.turtle_exception.client.api.entities.containers.messages;

import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.api.entities.containers.ITurtleContainer;
import de.turtle_exception.client.api.entities.messages.SyncMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/** Represents an object that can cache {@link SyncMessage SyncMessages}. */
public interface IMessageContainer extends ITurtleContainer {
    /**
     * Returns an immutable List of all cached {@link SyncMessage} objects.
     * @return List of cached SyncMessages.
     */
    @NotNull List<SyncMessage> getSyncMessages();

    /**
     * Returns a single {@link SyncMessage} specified by its id, or {@code null} if no such object is stored in the
     * underlying cache.
     * @param id The unique id of the SyncMessage.
     * @return The requested SyncMessage (may be {@code null}).
     * @see SyncMessage#getId()
     */
    @Nullable SyncMessage getSyncMessageById(long id);

    @Override
    default @NotNull List<Turtle> getTurtles() {
        return List.copyOf(new ArrayList<>(getSyncMessages()));
    }

    @Override
    default @Nullable Turtle getTurtleById(long id) {
        return getSyncMessageById(id);
    }
}
