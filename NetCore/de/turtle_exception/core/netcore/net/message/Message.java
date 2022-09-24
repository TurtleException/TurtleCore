package de.turtle_exception.core.netcore.net.message;

import de.turtle_exception.core.netcore.net.route.Route;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

public class Message {
    private final int callbackCode;
    private final @NotNull Route route;
    private final @NotNull String content;
    private final @NotNull BiConsumer<Message, Message> finalizer;

    public Message(int callbackCode, @NotNull Route route, @Nullable String content, @Nullable BiConsumer<Message, Message> finalizer) {
        this.callbackCode = callbackCode;
        this.route = route;
        this.content = content != null ? content : "";
        this.finalizer = finalizer != null ? finalizer : (self, response) -> { };
    }

    public void handleResponse(Message response) throws IllegalArgumentException {
        if (!this.route.isTerminating() && response != null)
            this.finalizer.accept(this, response);
        throw new IllegalArgumentException("This route is " + (this.route.isTerminating() ? "" : "not ") + "terminating.");
    }

    public int getCallbackCode() {
        return callbackCode;
    }

    public @NotNull Route getRoute() {
        return route;
    }

    public @NotNull String getCommand() {
        String routeCommand = this.route.getCommand();
        return routeCommand != null ? routeCommand : "";
    }

    public @NotNull String getContent() {
        return content;
    }
}
