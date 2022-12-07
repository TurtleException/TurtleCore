package de.turtle_exception.client.api.request.entities.messages;

import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.entities.attributes.MessageFormat;
import de.turtle_exception.client.api.entities.messages.IChannel;
import de.turtle_exception.client.api.entities.messages.SyncChannel;
import de.turtle_exception.client.api.entities.messages.SyncMessage;
import de.turtle_exception.client.api.request.Action;

import java.util.Collection;

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
     * Sets the format of this Message to the provided MessageFormat.
     * @param format Message format.
     * @return This SyncMessageAction for chaining convenience.
     */
    SyncMessageAction setFormat(MessageFormat format);

    /**
     * Sets the author of this Message to the provided User.
     * @param user Message author.
     * @return This SyncMessageAction for chaining convenience.
     */
    SyncMessageAction setAuthor(User user);

    /**
     * Sets the raw content of this Message to the provided String.
     * @param content Raw Message content.
     * @return This SyncMessageAction for chaining convenience.
     */
    SyncMessageAction setContent(String content);

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
}
