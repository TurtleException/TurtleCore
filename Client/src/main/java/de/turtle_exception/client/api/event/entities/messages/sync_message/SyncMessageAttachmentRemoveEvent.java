package de.turtle_exception.client.api.event.entities.messages.sync_message;

import de.turtle_exception.client.api.entities.messages.SyncMessage;
import de.turtle_exception.client.api.event.entities.EntityUpdateEntryEvent;
import de.turtle_exception.client.internal.data.annotations.Keys;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Function;

public class SyncMessageAttachmentRemoveEvent extends SyncMessageEvent implements EntityUpdateEntryEvent<SyncMessage, Long> {
    protected final long attachmentId;

    public SyncMessageAttachmentRemoveEvent(@NotNull SyncMessage message, long attachmentId) {
        super(message);
        this.attachmentId = attachmentId;
    }

    public long getAttachmentId() {
        return attachmentId;
    }

    /* - EntityUpdateEntryEvent - */

    @Override
    public final @NotNull String getKey() {
        return Keys.Messages.SyncMessage.ATTACHMENTS;
    }

    @Override
    public final @NotNull Collection<Long> getCollection() {
        return getMessage().getAttachmentIds();
    }

    @Override
    public final @NotNull Function<Long, Object> getMutator() {
        return l -> l;
    }
}
