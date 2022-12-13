package de.turtle_exception.client.internal.entities.messages;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.attributes.MessageFormat;
import de.turtle_exception.client.api.entities.messages.SyncMessage;
import de.turtle_exception.client.api.event.entities.messages.sync_message.*;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.internal.entities.TurtleImpl;
import de.turtle_exception.client.internal.event.UpdateHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SyncMessageImpl extends TurtleImpl implements SyncMessage {
    private MessageFormat format;
    private long author;
    private String content;
    private Long reference;
    private long channel;
    private long source;
    private ArrayList<Long> attachments;

    public SyncMessageImpl(@NotNull TurtleClient client, long id, MessageFormat format, long author, String content, Long reference, long channel, long source, ArrayList<Long> attachments) {
        super(client, id);

        this.format      = format;
        this.author      = author;
        this.content     = content;
        this.reference   = reference;
        this.channel     = channel;
        this.source      = source;
        this.attachments = attachments;
    }

    @Override
    public @NotNull TurtleImpl handleUpdate(@NotNull JsonObject json) {
        this.apply(json, Keys.Messages.SyncMessage.FORMAT, element -> {
            MessageFormat old = this.format;
            this.format = MessageFormat.of(json.getAsByte());
            this.fireEvent(new SyncMessageUpdateFormatEvent(this, old, this.format));
        });
        this.apply(json, Keys.Messages.SyncMessage.AUTHOR, element -> {
            long old = this.author;
            this.author = element.getAsLong();
            this.fireEvent(new SyncMessageUpdateAuthorEvent(this, old, this.author));
        });
        this.apply(json, Keys.Messages.SyncMessage.CONTENT, element -> {
            String old = this.content;
            this.content = element.getAsString();
            this.fireEvent(new SyncMessageUpdateContentEvent(this, old, this.content));
        });
        this.apply(json, Keys.Messages.SyncMessage.REFERENCE, element -> {
            long old = this.reference;
            this.reference = element.getAsLong();
            this.fireEvent(new SyncMessageUpdateReferenceEvent(this, old, this.reference));
        });
        this.apply(json, Keys.Messages.SyncMessage.CHANNEL, element -> {
            long old = this.channel;
            this.channel = element.getAsLong();
            this.fireEvent(new SyncMessageUpdateChannelEvent(this, old, this.channel));
        });
        this.apply(json, Keys.Messages.SyncMessage.SOURCE, element -> {
            long old = this.source;
            this.source = element.getAsLong();
            this.fireEvent(new SyncMessageUpdateSourceEvent(this, old, this.source));
        });
        this.apply(json, Keys.Messages.SyncMessage.ATTACHMENTS, element -> {
            ArrayList<Long> old  = this.attachments;
            ArrayList<Long> list = new ArrayList<>();
            for (JsonElement entry : element.getAsJsonArray())
                list.add(entry.getAsLong());
            this.attachments = list;
            UpdateHelper.ofSyncMessageAttachments(this, old, list);
        });
        return this;
    }

    /* - FORMAT - */

    @Override
    public @NotNull MessageFormat getMessageFormat() {
        return this.format;
    }

    @Override
    public @NotNull Action<SyncMessage> modifyMessageFormat(@NotNull MessageFormat format) {
        return getClient().getProvider().patch(this, Keys.Messages.SyncMessage.FORMAT, format).andThenParse(SyncMessage.class);
    }

    /* - AUTHOR - */

    @Override
    public long getAuthorId() {
        return this.author;
    }

    @Override
    public @NotNull Action<SyncMessage> modifyAuthor(long user) {
        return getClient().getProvider().patch(this, Keys.Messages.SyncMessage.AUTHOR, user).andThenParse(SyncMessage.class);
    }

    /* - CONTENT - */

    @Override
    public @NotNull String getContent() {
        return this.content;
    }

    @Override
    public @NotNull String getContent(@NotNull MessageFormat format) {
        // TODO: apply format
        return this.content;
    }

    @Override
    public @NotNull Action<SyncMessage> modifyContent(@NotNull String content) {
        return getClient().getProvider().patch(this, Keys.Messages.SyncMessage.CONTENT, content).andThenParse(SyncMessage.class);
    }

    /* - REFERENCE - */

    @Override
    public @Nullable Long getReferenceId() {
        return this.reference;
    }

    @Override
    public @NotNull Action<SyncMessage> modifyReference(@Nullable Long reference) {
        return getClient().getProvider().patch(this, Keys.Messages.SyncMessage.REFERENCE, reference).andThenParse(SyncMessage.class);
    }

    /* - CHANNEL - */

    @Override
    public long getChannelId() {
        return this.channel;
    }

    @Override
    public @NotNull Action<SyncMessage> modifyChannel(@NotNull Long channel) {
        return getClient().getProvider().patch(this, Keys.Messages.SyncMessage.CHANNEL, channel).andThenParse(SyncMessage.class);
    }

    /* - SOURCE - */

    @Override
    public long getSourceId() {
        return this.source;
    }

    @Override
    public @NotNull Action<SyncMessage> modifySource(@NotNull Long source) {
        return getClient().getProvider().patch(this, Keys.Messages.SyncMessage.SOURCE, source).andThenParse(SyncMessage.class);
    }

    /* - ATTACHMENTS - */

    @Override
    public @NotNull List<Long> getAttachmentIds() {
        return this.attachments;
    }

    @Override
    public @NotNull Action<SyncMessage> addAttachment(long attachment) {
        return getClient().getProvider().patchEntryAdd(this, Keys.Messages.SyncMessage.ATTACHMENTS, attachment).andThenParse(SyncMessage.class);
    }

    @Override
    public @NotNull Action<SyncMessage> removeAttachment(long attachment) {
        return getClient().getProvider().patchEntryDel(this, Keys.Messages.SyncMessage.ATTACHMENTS, attachment).andThenParse(SyncMessage.class);
    }
}
