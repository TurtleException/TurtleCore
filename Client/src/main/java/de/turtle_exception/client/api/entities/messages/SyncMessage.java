package de.turtle_exception.client.api.entities.messages;

import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.entities.attributes.MessageFormat;
import de.turtle_exception.client.internal.data.annotations.Key;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.internal.data.annotations.Resource;
import de.turtle_exception.client.internal.data.annotations.Types;
import org.jetbrains.annotations.NotNull;

@Resource(path = "messages", builder = "buildMessage")
@SuppressWarnings("unused")
public interface SyncMessage extends Turtle {
    @NotNull MessageFormat getMessageFormat();

    @Key(name = Keys.Messages.SyncMessage.FORMAT, sqlType = Types.Messages.SyncMessage.FORMAT)
    default byte getMessageFormatCode() {
        return this.getMessageFormat().getCode();
    }

    @Key(name = Keys.Messages.SyncMessage.AUTHOR, sqlType = Types.Messages.SyncMessage.AUTHOR)
    @NotNull User getAuthor();

    @Key(name = Keys.Messages.SyncMessage.CONTENT, sqlType = Types.Messages.SyncMessage.CONTENT)
    @NotNull String getContent();

    @NotNull String getContent(@NotNull MessageFormat format);

    // TODO: source (+ source-reference), referenced message, attachments, recipients
}
