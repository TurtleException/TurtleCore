package de.turtle_exception.client.internal.entities.messages;

import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.messages.Attachment;
import de.turtle_exception.client.internal.data.annotations.Key;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.internal.data.annotations.Types;
import de.turtle_exception.client.internal.entities.TurtleImpl;
import org.jetbrains.annotations.NotNull;

public class AttachmentImpl extends TurtleImpl implements Attachment {
    private final long snowflake;
    private final String url;
    private final String proxyUrl;
    private final String fileName;
    private final String contentType;
    private final String description;
    private final long size;
    private final int height;
    private final int width;
    private final boolean ephemeral;

    public AttachmentImpl(long id, TurtleClient client, long snowflake, String url, String proxyUrl, String fileName, String contentType, String description, long size, int height, int width, boolean ephemeral) {
        super(client, id);

        this.snowflake = snowflake;
        this.url = url;
        this.proxyUrl = proxyUrl;
        this.fileName = fileName;
        this.contentType = contentType;
        this.description = description;
        this.size = size;
        this.height = height;
        this.width = width;
        this.ephemeral = ephemeral;
    }

    @Override
    public @NotNull TurtleImpl handleUpdate(@NotNull JsonObject json) {
        // this resource is immutable
        return this;
    }

    /* - - - */

    @Override
    public long getId() {
        return id;
    }

    @Override
    public @NotNull TurtleClient getClient() {
        return client;
    }

    /* - - - */

    @Key(name = Keys.Messages.Attachment.SNOWFLAKE, sqlType = Types.Messages.Attachment.SNOWFLAKE)
    public long getSnowflake() {
        return snowflake;
    }

    @Key(name = Keys.Messages.Attachment.URL, sqlType = Types.Messages.Attachment.URL)
    public String getUrl() {
        return url;
    }

    @Key(name = Keys.Messages.Attachment.PROXY_URL, sqlType = Types.Messages.Attachment.PROXY_URL)
    public String getProxyUrl() {
        return proxyUrl;
    }

    @Key(name = Keys.Messages.Attachment.FILE_NAME, sqlType = Types.Messages.Attachment.FILE_NAME)
    public String getFileName() {
        return fileName;
    }

    @Key(name = Keys.Messages.Attachment.CONTENT_TYPE, sqlType = Types.Messages.Attachment.CONTENT_TYPE)
    public String getContentType() {
        return contentType;
    }

    @Key(name = Keys.Messages.Attachment.DESCRIPTION, sqlType = Types.Messages.Attachment.DESCRIPTION)
    public String getDescription() {
        return description;
    }

    @Key(name = Keys.Messages.Attachment.SIZE, sqlType = Types.Messages.Attachment.SIZE)
    public long getSize() {
        return size;
    }

    @Key(name = Keys.Messages.Attachment.HEIGHT, sqlType = Types.Messages.Attachment.HEIGHT)
    public int getHeight() {
        return height;
    }

    @Key(name = Keys.Messages.Attachment.WIDTH, sqlType = Types.Messages.Attachment.WIDTH)
    public int getWidth() {
        return width;
    }

    @Key(name = Keys.Messages.Attachment.EPHEMERAL, sqlType = Types.Messages.Attachment.EPHEMERAL)
    public boolean isEphemeral() {
        return ephemeral;
    }
}
