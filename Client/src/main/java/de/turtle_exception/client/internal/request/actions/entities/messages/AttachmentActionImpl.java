package de.turtle_exception.client.internal.request.actions.entities.messages;

import com.google.gson.JsonObject;
import de.turtle_exception.client.api.entities.messages.Attachment;
import de.turtle_exception.client.api.request.entities.messages.AttachmentAction;
import de.turtle_exception.client.internal.Provider;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.internal.request.actions.EntityAction;
import org.jetbrains.annotations.NotNull;

public class AttachmentActionImpl extends EntityAction<Attachment> implements AttachmentAction {
    private Long snowflake;
    private String url;
    private String proxyUrl;
    private String fileName;
    private String contentType = null;
    private String description = null;
    private Long size;
    private Integer height = -1;
    private Integer width = -1;
    private boolean ephemeral = false;

    public AttachmentActionImpl(@NotNull Provider provider) {
        super(provider, Attachment.class);

        this.checks.add(json -> { json.get(Keys.Messages.Attachment.SNOWFLAKE).getAsLong(); });
        this.checks.add(json -> { json.get(Keys.Messages.Attachment.URL).getAsString(); });
        this.checks.add(json -> { json.get(Keys.Messages.Attachment.PROXY_URL).getAsString(); });
        this.checks.add(json -> { json.get(Keys.Messages.Attachment.FILE_NAME).getAsString(); });
        this.checks.add(json -> { json.get(Keys.Messages.Attachment.SIZE).getAsLong(); });
        this.checks.add(json -> {
            if (json.get(Keys.Messages.Attachment.HEIGHT).getAsLong() < -1)
                throw new IllegalArgumentException();
        });
        this.checks.add(json -> {
            if (json.get(Keys.Messages.Attachment.WIDTH).getAsLong() < -1)
                throw new IllegalArgumentException();
        });
        this.checks.add(json -> { json.get(Keys.Messages.Attachment.EPHEMERAL).getAsBoolean(); });
    }

    @Override
    protected void updateContent() {
        this.content = new JsonObject();
        this.content.addProperty(Keys.Messages.Attachment.SNOWFLAKE, snowflake);
        this.content.addProperty(Keys.Messages.Attachment.URL, url);
        this.content.addProperty(Keys.Messages.Attachment.PROXY_URL, proxyUrl);
        this.content.addProperty(Keys.Messages.Attachment.FILE_NAME, fileName);
        if (this.contentType != null)
            this.content.addProperty(Keys.Messages.Attachment.CONTENT_TYPE, contentType);
        if (this.description != null)
            this.content.addProperty(Keys.Messages.Attachment.DESCRIPTION, description);
        this.content.addProperty(Keys.Messages.Attachment.SIZE, size);
        this.content.addProperty(Keys.Messages.Attachment.HEIGHT, height);
        this.content.addProperty(Keys.Messages.Attachment.WIDTH, width);
        this.content.addProperty(Keys.Messages.Attachment.EPHEMERAL, ephemeral);
    }

    /* - - - */

    @Override
    public AttachmentAction setSnowflake(long snowflake) {
        this.snowflake = snowflake;
        return this;
    }

    @Override
    public AttachmentAction setUrl(String url) {
        this.url = url;
        return this;
    }

    @Override
    public AttachmentAction setProxyUrl(String proxyUrl) {
        this.proxyUrl = proxyUrl;
        return this;
    }

    @Override
    public AttachmentAction setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    @Override
    public AttachmentAction setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    @Override
    public AttachmentAction setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public AttachmentAction setSize(long size) {
        this.size = size;
        return this;
    }

    @Override
    public AttachmentAction setHeight(int height) {
        this.height = height;
        return this;
    }

    @Override
    public AttachmentAction setWidth(int width) {
        this.width = width;
        return this;
    }

    @Override
    public AttachmentAction setEphemeral() {
        this.ephemeral = true;
        return this;
    }
}
