package de.turtle_exception.client.api.request.entities.messages;

import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.entities.messages.Attachment;
import de.turtle_exception.client.api.entities.messages.IChannel;
import de.turtle_exception.client.api.entities.messages.SyncChannel;
import de.turtle_exception.client.api.entities.messages.SyncMessage;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.fancyformat.Format;
import de.turtle_exception.fancyformat.FormatText;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * A SyncMessageAction is an Action that requests the creation of a new {@link SyncMessage Message}, according to the
 * arguments this Action holds. If any required field is missing the server will reject the request and respond with an
 * error. Required fields are all attributes that are not a subclass of {@link Collection}, as these are set to an empty
 * Collection by default.
 * @see TurtleClient#createMessage()
 */
@SuppressWarnings("unused")
public interface SyncMessageAction extends Action<SyncMessage> {
    /**
     * Sets the author of this Message to the provided User.
     * @param user Message author.
     * @return This SyncMessageAction for chaining convenience.
     */
    SyncMessageAction setAuthor(User user);

    /**
     * Sets the content of this Message to the provided FormatText.
     * @param content Message content.
     * @return This SyncMessageAction for chaining convenience.
     */
    SyncMessageAction setContent(FormatText content);

    /**
     * Sets the content of this Message to the provided String & Format.
     * @param content Message content.
     * @return This SyncMessageAction for chaining convenience.
     */
    default SyncMessageAction setContent(@NotNull String content, @NotNull Format format) {
        return this.setContent(this.getClient().getFormatter().newText(content, format));
    }

    /**
     * Sets the referenced message (id) of this Message to the provided id.
     * @param id ID of the referenced Message (May be {@code null}).
     * @return This SyncMessageAction for chaining convenience.
     */
    SyncMessageAction setReference(Long id);

    /**
     * Sets the channel of this Message to the provided Channel.
     * @param channel Message channel.
     * @return This SyncMessageAction for chaining convenience.
     */
    SyncMessageAction setChannel(SyncChannel channel);

    /**
     * Sets the source of this Message to the provided Channel.
     * @param source Message source channel.
     * @return This SyncMessageAction for chaining convenience.
     */
    SyncMessageAction setSource(IChannel source);

    /**
     * Sets the List of ids that each represent an {@link Attachment} of this message.
     * The existing List will be overwritten.
     * @param attachments List of Attachment ids.
     * @return This SyncMessageAction for chaining convenience.
     */
    SyncMessageAction setAttachmentIds(@NotNull List<Long> attachments);

    /**
     * Sets the List of message {@link Attachment Attachments}.
     * The existing List will be overwritten.
     * @param attachments List of Attachments.
     * @return This SyncMessageAction for chaining convenience.
     */
    default SyncMessageAction setAttachments(@NotNull List<Attachment> attachments) {
        return this.setAttachmentIds(attachments.stream().map(Attachment::getId).toList());
    }

    /**
     * Sets the List of ids that each represent an {@link Attachment} of this message.
     * The existing List will be overwritten.
     * @param attachments Array of Attachment ids.
     * @return This SyncMessageAction for chaining convenience.
     */
    default SyncMessageAction setAttachmentIds(@NotNull Long... attachments) {
        return this.setAttachmentIds(Arrays.asList(attachments));
    }

    /**
     * Sets the List of message {@link Attachment Attachments}.
     * The existing List will be overwritten.
     * @param attachments Array of Attachments.
     * @return This SyncMessageAction for chaining convenience.
     */
    default SyncMessageAction setAttachments(@NotNull Attachment... attachments) {
        return this.setAttachments(Arrays.asList(attachments));
    }

    /**
     * Adds the provided {@code long} to the List of ids that each represent an {@link Attachment} of this message.
     * @param attachment Attachment id.
     * @return This SyncMessageAction for chaining convenience.
     */
    SyncMessageAction addAttachmentId(long attachment);

    /**
     * Adds the provided {@link Attachment} to the List of message Attachments.
     * @param attachment Some Attachment.
     * @return This SyncMessageAction for chaining convenience.
     */
    default SyncMessageAction addAttachment(@NotNull Attachment attachment) {
        return this.addAttachmentId(attachment.getId());
    }

    /**
     * Removes the provided {@code long} from the List of ids that each represent an {@link Attachment} of this message.
     * @param attachment Attachment id.
     * @return This SyncMessageAction for chaining convenience.
     */
    SyncMessageAction removeUseId(long attachment);

    /**
     * Removes the provided {@link Attachment} from the List of message Attachments.
     * @param attachment Some Attachment.
     * @return This SyncMessageAction for chaining convenience.
     */
    default SyncMessageAction removeAttachment(@NotNull Attachment attachment) {
        return this.removeUseId(attachment.getId());
    }
}
