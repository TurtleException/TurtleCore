package de.turtle_exception.client.api.event.entities.messages.attachment;

import de.turtle_exception.client.api.entities.messages.Attachment;
import de.turtle_exception.client.api.event.entities.EntityCreateEvent;
import org.jetbrains.annotations.NotNull;

public class AttachmentCreateEvent extends AttachmentEvent implements EntityCreateEvent<Attachment> {
    public AttachmentCreateEvent(@NotNull Attachment attachment) {
        super(attachment);
    }
}
