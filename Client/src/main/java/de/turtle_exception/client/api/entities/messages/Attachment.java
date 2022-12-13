package de.turtle_exception.client.api.entities.messages;

import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.internal.data.annotations.Key;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.internal.data.annotations.Resource;
import de.turtle_exception.client.internal.data.annotations.Types;

@Resource(path = "attachments", builder = "buildAttachment")
@SuppressWarnings("unused")
public interface Attachment extends Turtle {
    @Key(name = Keys.Messages.Attachment.SNOWFLAKE, sqlType = Types.Messages.Attachment.SNOWFLAKE)
    long getSnowflake();

    @Key(name = Keys.Messages.Attachment.URL, sqlType = Types.Messages.Attachment.URL)
    String getUrl();

    @Key(name = Keys.Messages.Attachment.PROXY_URL, sqlType = Types.Messages.Attachment.PROXY_URL)
    String getProxyUrl();

    @Key(name = Keys.Messages.Attachment.FILE_NAME, sqlType = Types.Messages.Attachment.FILE_NAME)
    String getFileName();

    @Key(name = Keys.Messages.Attachment.CONTENT_TYPE, sqlType = Types.Messages.Attachment.CONTENT_TYPE)
    String getContentType();

    @Key(name = Keys.Messages.Attachment.DESCRIPTION, sqlType = Types.Messages.Attachment.DESCRIPTION)
    String getDescription();

    @Key(name = Keys.Messages.Attachment.SIZE, sqlType = Types.Messages.Attachment.SIZE)
    long getSize();

    @Key(name = Keys.Messages.Attachment.HEIGHT, sqlType = Types.Messages.Attachment.HEIGHT)
    int getHeight();

    @Key(name = Keys.Messages.Attachment.WIDTH, sqlType = Types.Messages.Attachment.WIDTH)
    int getWidth();

    @Key(name = Keys.Messages.Attachment.EPHEMERAL, sqlType = Types.Messages.Attachment.EPHEMERAL)
    boolean isEphemeral();
}
