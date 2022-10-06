package de.turtle_exception.client.api.event.net;

import de.turtle_exception.client.api.event.Event;
import de.turtle_exception.client.api.requests.Request;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class RequestEvent extends Event {
    protected final Request<?> request;

    public RequestEvent(@NotNull Request<?> request) {
        super(request.getClient());
        this.request = request;
    }

    public @NotNull Request<?> getRequest() {
        return request;
    }
}
