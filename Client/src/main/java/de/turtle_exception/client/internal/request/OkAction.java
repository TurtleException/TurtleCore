package de.turtle_exception.client.internal.request;

import com.google.gson.JsonObject;
import de.turtle_exception.client.internal.net.message.Message;
import de.turtle_exception.client.internal.net.message.Route;
import org.jetbrains.annotations.NotNull;

public class OkAction extends NetActionImpl<Void> {
    public OkAction(@NotNull Message message) {
        super(message.getConnection(), Route.OK, new JsonObject());
        this.conversation = message.getConversation();
    }
}
