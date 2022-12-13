package de.turtle_exception.client.internal.request.actions.entities.messages;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.entities.messages.IChannel;
import de.turtle_exception.client.api.entities.messages.SyncChannel;
import de.turtle_exception.client.api.entities.messages.SyncMessage;
import de.turtle_exception.client.api.request.entities.messages.SyncMessageAction;
import de.turtle_exception.client.internal.Provider;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.internal.request.actions.EntityAction;
import de.turtle_exception.fancyformat.Format;
import de.turtle_exception.fancyformat.FormatText;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SyncMessageActionImpl extends EntityAction<SyncMessage> implements SyncMessageAction {
    protected User author;
    protected FormatText msgContent;
    protected Long reference;
    protected SyncChannel channel;
    protected IChannel source;
    protected ArrayList<Long> attachments;

    @SuppressWarnings("CodeBlock2Expr")
    public SyncMessageActionImpl(@NotNull Provider provider) {
        super(provider, SyncMessage.class);

        this.checks.add(json -> { json.get(Keys.Messages.SyncMessage.FORMAT).getAsByte(); });
        this.checks.add(json -> { json.get(Keys.Messages.SyncMessage.AUTHOR).getAsLong(); });
        this.checks.add(json -> { json.get(Keys.Messages.SyncMessage.CONTENT).getAsString(); });
        this.checks.add(json -> { json.get(Keys.Messages.SyncMessage.CHANNEL).getAsLong(); });
        this.checks.add(json -> { json.get(Keys.Messages.SyncMessage.SOURCE).getAsLong(); });
        this.checks.add(json -> {
            JsonArray arr = json.get(Keys.Messages.SyncMessage.ATTACHMENTS).getAsJsonArray();
            for (JsonElement entry : arr)
                entry.getAsLong();
        });
    }

    @Override
    protected void updateContent() {
        this.content = new JsonObject();
        this.content.addProperty(Keys.Messages.SyncMessage.AUTHOR, author.getId());
        this.content.addProperty(Keys.Messages.SyncMessage.CONTENT, msgContent.toString(Format.TURTLE));
        this.content.addProperty(Keys.Messages.SyncMessage.REFERENCE, reference);
        this.content.addProperty(Keys.Messages.SyncMessage.CHANNEL, channel.getId());
        this.content.addProperty(Keys.Messages.SyncMessage.SOURCE, source.getId());

        JsonArray arr = new JsonArray();
        for (Long attachment : this.attachments)
            arr.add(attachment);
        this.content.add(Keys.Messages.SyncMessage.ATTACHMENTS, arr);
    }

    /* - - - */

    @Override
    public SyncMessageAction setAuthor(User user) {
        this.author = user;
        return this;
    }

    @Override
    public SyncMessageAction setContent(FormatText content) {
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

    /* - ATTACHMENTS - */

    @Override
    public SyncMessageAction setAttachmentIds(@NotNull List<Long> attachments) {
        this.attachments = new ArrayList<>(attachments);
        return this;
    }

    @Override
    public SyncMessageAction addAttachmentId(long attachment) {
        this.attachments.add(attachment);
        return this;
    }

    @Override
    public SyncMessageAction removeUseId(long attachment) {
        this.attachments.remove(attachment);
        return this;
    }
}
