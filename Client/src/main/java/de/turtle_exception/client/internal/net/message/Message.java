package de.turtle_exception.client.internal.net.message;

import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.internal.net.route.CompiledRoute;
import de.turtle_exception.client.internal.net.route.Routes;
import org.jetbrains.annotations.NotNull;

public abstract class Message {
    protected final @NotNull TurtleClient client;

    /**
     * The compiled Route, also containing the message content.
     */
    protected final @NotNull CompiledRoute route;

    protected final long deadline;
    protected final long conversation;

    public boolean done      = false;
    public boolean cancelled = false;

    public Message(@NotNull TurtleClient client, long conversation, @NotNull CompiledRoute route, long deadline) {
        this.client = client;
        this.route        = route;
        this.deadline     = deadline;
        this.conversation = conversation;
    }

    public @NotNull TurtleClient getClient() {
        return client;
    }

    public @NotNull CompiledRoute getRoute() {
        return route;
    }

    public boolean isError() {
        return route.isRoute(Routes.ERROR);
    }

    public boolean isTerminating() {
        return this.route.route().getMethod().isTerminating();
    }

    public long getDeadline() {
        return deadline;
    }

    public long getConversation() {
        return conversation;
    }
}
