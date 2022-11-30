package de.turtle_exception.client.internal.entities.messages;

import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.entities.attributes.MessageFormat;
import de.turtle_exception.client.api.entities.messages.IChannel;
import de.turtle_exception.client.api.entities.messages.SyncChannel;
import de.turtle_exception.client.api.entities.messages.SyncMessage;
import de.turtle_exception.client.internal.entities.TurtleImpl;
import org.jetbrains.annotations.NotNull;

public class SyncMessageImpl extends TurtleImpl implements SyncMessage {
    private MessageFormat format;
    private User author;
    private String content;
    private long reference;
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

    /* - - - */

    @Override
    public @NotNull MessageFormat getMessageFormat() {
        return this.format;
    }

    @Override
    public @NotNull User getAuthor() {
        return this.author;
    }

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
    public long getReference() {
        return this.reference;
    }

    @Override
    public @NotNull SyncChannel getChannel() {
        return this.channel;
    }

    @Override
    public @NotNull IChannel getSource() {
        return this.source;
    }
}
