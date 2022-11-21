package de.turtle_exception.client.api.entities.messages;

import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.entities.attributes.MessageFormat;
import de.turtle_exception.client.internal.data.annotations.Key;
import de.turtle_exception.client.internal.data.annotations.Resource;
import org.jetbrains.annotations.NotNull;

@Resource(path = "messages", builder = "buildMessage")
@SuppressWarnings("unused")
public interface SyncMessage extends Turtle {
    @Key(name = "format")
    @NotNull MessageFormat getMessageFormat();

    @Key(name = "author")
    @NotNull User getAuthor();

    @Key(name = "content")
    @NotNull String getContent();

    @NotNull String getContent(@NotNull MessageFormat format);

    // TODO: source (+ source-reference), referenced message, attachments, recipients
}
