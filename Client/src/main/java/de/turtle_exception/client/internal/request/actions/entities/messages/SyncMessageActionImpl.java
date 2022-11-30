package de.turtle_exception.client.internal.request.actions.entities.messages;

import com.google.gson.JsonObject;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.entities.attributes.MessageFormat;
import de.turtle_exception.client.api.entities.messages.IChannel;
import de.turtle_exception.client.api.entities.messages.SyncChannel;
import de.turtle_exception.client.api.entities.messages.SyncMessage;
import de.turtle_exception.client.api.request.entities.messages.SyncMessageAction;
import de.turtle_exception.client.internal.Provider;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.internal.request.actions.EntityAction;
import org.jetbrains.annotations.NotNull;

public class SyncMessageActionImpl extends EntityAction<SyncMessage> implements SyncMessageAction {
    protected MessageFormat format;
    protected User author;
    protected String msgContent;
    protected Long reference;
    protected SyncChannel channel;
    protected IChannel source;

    public SyncMessageActionImpl(@NotNull Provider provider) {
        super(provider, SyncMessage.class);

        this.checks.add(json -> { MessageFormat.of(json.get(Keys.Messages.SyncMessage.FORMAT).getAsByte()); });
        this.checks.add(json -> { json.get(Keys.Messages.SyncMessage.AUTHOR).getAsLong(); });
        this.checks.add(json -> { json.get(Keys.Messages.SyncMessage.CONTENT).getAsString(); });
        this.checks.add(json -> { json.get(Keys.Messages.SyncMessage.REFERENCE).getAsLong(); });
        this.checks.add(json -> { json.get(Keys.Messages.SyncMessage.CHANNEL).getAsLong(); });
        this.checks.add(json -> { json.get(Keys.Messages.SyncMessage.SOURCE).getAsLong(); });
    }

    @Override
    protected void updateContent() {
        this.content = new JsonObject();
        this.content.addProperty(Keys.Messages.SyncMessage.FORMAT, format.getCode());
        this.content.addProperty(Keys.Messages.SyncMessage.AUTHOR, author.getId());
        this.content.addProperty(Keys.Messages.SyncMessage.CONTENT, msgContent);
        this.content.addProperty(Keys.Messages.SyncMessage.REFERENCE, reference);
        this.content.addProperty(Keys.Messages.SyncMessage.CHANNEL, channel.getId());
        this.content.addProperty(Keys.Messages.SyncMessage.SOURCE, source.getId());
    }

    /* - - - */

    @Override
    public SyncMessageAction setFormat(MessageFormat format) {
        this.format = format;
        return this;
    }

    @Override
    public SyncMessageAction setAuthor(User user) {
        this.author = user;
        return this;
    }

    @Override
    public SyncMessageAction setContent(String content) {
        this.msgContent = content;
        return this;
    }

    @Override
    public SyncMessageAction setReference(Long id) {
        this.reference = id;
        return this;
    }

    @Override
    public SyncMessageAction setChannel(SyncChannel channel) {
        this.channel = channel;
        return this;
    }

    @Override
    public SyncMessageAction setSource(IChannel source) {
        this.source = source;
        return this;
    }
}
