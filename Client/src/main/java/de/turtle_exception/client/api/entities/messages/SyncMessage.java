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

/**
 * A synchronized message that originates from an {@link IChannel} and will be shared with all other
 * {@link IChannel IChannels} the governing {@link SyncChannel} manages.
 */
@Resource(path = "messages", builder = "buildMessage")
@SuppressWarnings("unused")
public interface SyncMessage extends Turtle {
    @Override
    default @NotNull Action<SyncMessage> update() {
        return this.getClient().retrieveTurtle(this.getId(), SyncMessage.class);
    }

    /* - FORMAT - */

    /**
     * Provides the MessageFormat of this message.
     * @return The Message format.
     */
    @NotNull MessageFormat getMessageFormat();

    /**
     * Creates an Action with the instruction to modify this Message's format and change it to the provided MessageFormat.
     * @param format New Message format.
     * @return Action that provides the modified {@link SyncMessage} on completion.
     */
    @NotNull Action<SyncMessage> modifyMessageFormat(@NotNull MessageFormat format);

    @Key(name = Keys.Messages.SyncMessage.FORMAT, sqlType = Types.Messages.SyncMessage.FORMAT)
    default byte getMessageFormatCode() {
        return this.getMessageFormat().getCode();
    }

    /* - AUTHOR - */

    /**
     * Provides the {@link User} author of this message.
     * @return The Message author.
     */
    @Key(name = Keys.Messages.SyncMessage.AUTHOR, sqlType = Types.Messages.SyncMessage.AUTHOR)
    @NotNull User getAuthor();

    /**
     * Creates an Action with the instruction to modify this Message's author id and change it to the provided id.
     * @param user New Message author id.
     * @return Action that provides the modified {@link SyncMessage} on completion.
     */
    @NotNull Action<SyncMessage> modifyAuthor(long user);

    /**
     * Creates an Action with the instruction to modify this Message's author and change it to the provided User.
     * @param user New Message author.
     * @return Action that provides the modified {@link SyncMessage} on completion.
     */
    default @NotNull Action<SyncMessage> modifyAuthor(@NotNull User user) {
        return this.modifyAuthor(user.getId());
    }

    /* - CONTENT - */

    /**
     * Provides the raw content of this message.
     * @return The Message content.
     */
    @Key(name = Keys.Messages.SyncMessage.CONTENT, sqlType = Types.Messages.SyncMessage.CONTENT)
    @NotNull String getContent();

    /**
     * Provides the content of this message formatted as specified by the provided {@link MessageFormat}.
     * @return The formatted Message content.
     */
    @NotNull String getContent(@NotNull MessageFormat format);

    /**
     * Creates an Action with the instruction to modify this Message's content and change it to the provided String.
     * @param content New Message content.
     * @return Action that provides the modified {@link SyncMessage} on completion.
     */
    @NotNull Action<SyncMessage> modifyContent(@NotNull String content);

    /* - REFERENCE - */

    /**
     * Provides the reference id of this message.
     * @return The Message format.
     */
    @Key(name = Keys.Messages.SyncMessage.REFERENCE, sqlType = Types.Messages.SyncMessage.REFERENCE)
    @Nullable Long getReference();

    /**
     * Creates an Action with the instruction to modify this Message's reference id and change it to the provided id.
     * @param reference New Message reference id.
     * @return Action that provides the modified {@link SyncMessage} on completion.
     */
    @NotNull Action<SyncMessage> modifyReference(@Nullable Long reference);

    /**
     * Creates an Action with the instruction to modify this Message's reference Message and change it to the provided
     * Message.
     * @param message New Message reference.
     * @return Action that provides the modified {@link SyncMessage} on completion.
     */
    default @NotNull Action<SyncMessage> modifyReference(SyncMessage message) {
        return this.modifyReference(message == null ? null : message.getId());
    }

    /* - CHANNEL - */

    /**
     * Provides the governing {@link SyncChannel} of this message.
     * @return The Message channel.
     */
    // not relational -> the id is stored with the message, that's it
    @Key(name = Keys.Messages.SyncMessage.CHANNEL, sqlType = Types.Messages.SyncMessage.CHANNEL)
    @NotNull SyncChannel getChannel();

    /**
     * Creates an Action with the instruction to modify this Message's channel id and change it to the provided id.
     * @param channel New Message channel.
     * @return Action that provides the modified {@link SyncMessage} on completion.
     */
    @NotNull Action<SyncMessage> modifyChannel(@NotNull Long channel);

    /**
     * Creates an Action with the instruction to modify this Message's channel and change it to the provided channel.
     * @param channel New Message channel.
     * @return Action that provides the modified {@link SyncMessage} on completion.
     */
    default @NotNull Action<SyncMessage> modifyChannel(@NotNull SyncChannel channel) {
        return this.modifyChannel(channel.getId());
    }

    /* - SOURCE - */

    /**
     * Provides the {@link IChannel} source of this Message.
     * @return The Message source.
     */
    // not relational -> the id is stored with the message, that's it
    @Key(name = Keys.Messages.SyncMessage.SOURCE, sqlType = Types.Messages.SyncMessage.SOURCE)
    @NotNull IChannel getSource();

    /**
     * Creates an Action with the instruction to modify this Message's source id and change it to the provided id.
     * @param source New Message source.
     * @return Action that provides the modified {@link SyncMessage} on completion.
     */
    @NotNull Action<SyncMessage> modifySource(@NotNull Long source);

    /**
     * Creates an Action with the instruction to modify this Message's source and change it to the provided IChannel.
     * @param source New Message source.
     * @return Action that provides the modified {@link SyncMessage} on completion.
     */
    default @NotNull Action<SyncMessage> modifySource(@NotNull IChannel source) {
        return this.modifySource(source.getId());
    }

    // TODO: attachments, recipients
}
