package de.turtle_exception.client.internal.entities.messages;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.entities.attributes.MessageFormat;
import de.turtle_exception.client.api.entities.messages.IChannel;
import de.turtle_exception.client.api.entities.messages.SyncChannel;
import de.turtle_exception.client.api.entities.messages.SyncMessage;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.internal.entities.TurtleImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SyncMessageImpl extends TurtleImpl implements SyncMessage {
    private MessageFormat format;
    private User author;
    private String content;
    private Long reference;
    private SyncChannel channel;
    private IChannel source;

    protected SyncMessageImpl(@NotNull TurtleClient client, long id, MessageFormat format, User author, String content, long reference, SyncChannel channel, IChannel source) {
        super(client, id);

        this.format    = format;
        this.author    = author;
        this.content   = content;
        this.reference = reference;
        this.channel   = channel;
        this.source    = source;
    }

    @Override
    public @NotNull TurtleImpl handleUpdate(@NotNull JsonObject json) {
        // TODO
        return null;
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
    public @NotNull User getAuthor() {
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
    public @Nullable Long getReference() {
        return this.reference;
    }

    @Override
    public @NotNull Action<SyncMessage> modifyReference(@Nullable Long reference) {
        return getClient().getProvider().patch(this, Keys.Messages.SyncMessage.REFERENCE, reference).andThenParse(SyncMessage.class);
    }

    /* - CHANNEL - */

    @Override
    public @NotNull SyncChannel getChannel() {
        return this.channel;
    }

    @Override
    public @NotNull Action<SyncMessage> modifyChannel(@NotNull Long channel) {
        return getClient().getProvider().patch(this, Keys.Messages.SyncMessage.CHANNEL, channel).andThenParse(SyncMessage.class);
    }

    /* - SOURCE - */

    @Override
    public @NotNull IChannel getSource() {
        return this.source;
    }

    @Override
    public @NotNull Action<SyncMessage> modifySource(@NotNull Long source) {
        return getClient().getProvider().patch(this, Keys.Messages.SyncMessage.SOURCE, source).andThenParse(SyncMessage.class);
    }
}
