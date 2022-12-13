package de.turtle_exception.client.api.event.entities.messages.attachment;

import de.turtle_exception.client.api.entities.messages.Attachment;
import de.turtle_exception.client.api.event.entities.EntityEvent;
import org.jetbrains.annotations.NotNull;

public class AttachmentEvent extends EntityEvent<Attachment> {
    public AttachmentEvent(@NotNull Attachment attachment) {
        super(attachment);
    }

    public @NotNull Attachment getAttachment() {
        return this.getEntity();
    }
}
