package de.turtle_exception.client.api.event.entities.messages.attachment;

import de.turtle_exception.client.api.entities.messages.Attachment;
import de.turtle_exception.client.api.event.entities.EntityDeleteEvent;
import org.jetbrains.annotations.NotNull;

public class AttachmentDeleteEvent extends AttachmentEvent implements EntityDeleteEvent<Attachment> {
    public AttachmentDeleteEvent(@NotNull Attachment attachment) {
        super(attachment);
    }
}
