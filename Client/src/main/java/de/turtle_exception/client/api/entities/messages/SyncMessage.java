package de.turtle_exception.client.api.entities.messages;

import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.entities.attributes.MessageFormat;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.data.annotations.Key;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.internal.data.annotations.Resource;
import de.turtle_exception.client.internal.data.annotations.Types;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Resource(path = "messages", builder = "buildMessage")
@SuppressWarnings("unused")
public interface SyncMessage extends Turtle {
    @Override
    default @NotNull Action<SyncMessage> update() {
        return this.getClient().retrieveMessage(this.getId());
    }

    @NotNull MessageFormat getMessageFormat();

    @NotNull Action<SyncMessage> modifyMessageFormat(@NotNull MessageFormat format);

    @Key(name = Keys.Messages.SyncMessage.FORMAT, sqlType = Types.Messages.SyncMessage.FORMAT)
    default byte getMessageFormatCode() {
        return this.getMessageFormat().getCode();
    }

    @Key(name = Keys.Messages.SyncMessage.AUTHOR, sqlType = Types.Messages.SyncMessage.AUTHOR)
    @NotNull User getAuthor();

    @NotNull Action<SyncMessage> modifyAuthor(long user);

    default @NotNull Action<SyncMessage> modifyAuthor(@NotNull User user) {
        return this.modifyAuthor(user.getId());
    }

    @Key(name = Keys.Messages.SyncMessage.CONTENT, sqlType = Types.Messages.SyncMessage.CONTENT)
    @NotNull String getContent();

    @NotNull String getContent(@NotNull MessageFormat format);

    @NotNull Action<SyncMessage> modifyContent(@NotNull String content);

    @Key(name = Keys.Messages.SyncMessage.REFERENCE, sqlType = Types.Messages.SyncMessage.REFERENCE)
    @Nullable Long getReference();

    @NotNull Action<SyncMessage> modifyReference(@Nullable Long reference);

    default @NotNull Action<SyncMessage> modifyReference(SyncMessage message) {
        return this.modifyReference(message == null ? null : message.getId());
    }

    // not relational -> the id is stored with the message, that's it
    @Key(name = Keys.Messages.SyncMessage.CHANNEL, sqlType = Types.Messages.SyncMessage.CHANNEL)
    @NotNull SyncChannel getChannel();

    @NotNull Action<SyncMessage> modifyChannel(@Nullable Long reference);

    default @NotNull Action<SyncMessage> modifyChannel(SyncChannel channel) {
        return this.modifyChannel(channel == null ? null : channel.getId());
    }

    // not relational -> the id is stored with the message, that's it
    @Key(name = Keys.Messages.SyncMessage.SOURCE, sqlType = Types.Messages.SyncMessage.SOURCE)
    @NotNull IChannel getSource();

    @NotNull Action<SyncMessage> modifySource(@NotNull Long source);

    default @NotNull Action<SyncMessage> modifySource(@NotNull IChannel source) {
        return this.modifySource(source.getId());
    }

    // TODO: attachments, recipients
}
