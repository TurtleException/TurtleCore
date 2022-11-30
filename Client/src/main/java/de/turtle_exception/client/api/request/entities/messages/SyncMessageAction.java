package de.turtle_exception.client.api.request.entities.messages;

import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.entities.attributes.MessageFormat;
import de.turtle_exception.client.api.entities.messages.IChannel;
import de.turtle_exception.client.api.entities.messages.SyncChannel;
import de.turtle_exception.client.api.entities.messages.SyncMessage;
import de.turtle_exception.client.api.request.Action;

public interface SyncMessageAction extends Action<SyncMessage> {
    SyncMessageAction setFormat(MessageFormat format);

    SyncMessageAction setAuthor(User user);

    SyncMessageAction setContent(String content);

    SyncMessageAction setReference(Long id);

    SyncMessageAction setChannel(SyncChannel channel);

    SyncMessageAction setSource(IChannel source);
}
