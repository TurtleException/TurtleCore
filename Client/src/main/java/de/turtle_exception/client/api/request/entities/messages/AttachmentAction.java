package de.turtle_exception.client.api.request.entities.messages;

import de.turtle_exception.client.api.entities.messages.Attachment;
import de.turtle_exception.client.api.request.Action;

public interface AttachmentAction extends Action<Attachment> {
    AttachmentAction setSnowflake(long snowflake);

    AttachmentAction setUrl(String url);

    AttachmentAction setProxyUrl(String proxyUrl);

    AttachmentAction setFileName(String fileName);

    AttachmentAction setContentType(String contentType);

    AttachmentAction setDescription(String description);

    AttachmentAction setSize(long size);

    AttachmentAction setHeight(int height);

    AttachmentAction setWidth(int width);

    AttachmentAction setEphemeral();
}
