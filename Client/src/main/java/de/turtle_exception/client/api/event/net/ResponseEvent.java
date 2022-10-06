package de.turtle_exception.client.api.event.net;

import de.turtle_exception.client.api.event.Event;
import de.turtle_exception.client.api.requests.Request;
import de.turtle_exception.core.net.message.InboundMessage;
import org.jetbrains.annotations.NotNull;

public class ResponseEvent extends Event {
    protected final Request<?> request;
    protected final InboundMessage msg;

    public ResponseEvent(@NotNull Request<?> request, @NotNull InboundMessage msg) {
        super(request.getClient());
        this.request = request;
        this.msg = msg;
    }

    public @NotNull Request<?> getRequest() {
        return request;
    }

    public @NotNull InboundMessage getMsg() {
        return msg;
    }
}
