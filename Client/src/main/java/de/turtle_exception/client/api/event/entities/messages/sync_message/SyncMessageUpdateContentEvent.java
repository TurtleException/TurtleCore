package de.turtle_exception.client.api.event.entities.messages.sync_message;

import de.turtle_exception.client.api.entities.messages.SyncMessage;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.fancyformat.FormatText;
import org.jetbrains.annotations.NotNull;

public class SyncMessageUpdateContentEvent extends SyncMessageUpdateEvent<FormatText> {
    public SyncMessageUpdateContentEvent(@NotNull SyncMessage message, FormatText oldValue, FormatText newValue) {
        super(message, Keys.Messages.SyncMessage.CONTENT, oldValue, newValue);
    }
}
