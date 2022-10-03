package de.turtle_exception.core.core.net.message;

import de.turtle_exception.core.core.TurtleCore;
import de.turtle_exception.core.core.net.route.CompiledRoute;
import de.turtle_exception.core.core.net.route.ContentType;
import org.jetbrains.annotations.NotNull;

public abstract class Message {
    protected final @NotNull TurtleCore core;

    /**
     * The compiled Route, also containing the message content.
     */
    protected final @NotNull CompiledRoute route;

    protected final long deadline;

    public boolean done      = false;
    public boolean cancelled = false;

    public Message(@NotNull TurtleCore core, @NotNull CompiledRoute route, long deadline) {
        this.core         = core;
        this.route        = route;
        this.deadline     = deadline;
    }

    public @NotNull TurtleCore getCore() {
        return core;
    }

    public @NotNull CompiledRoute getRoute() {
        return route;
    }

    public boolean isError() {
        return route.contentType().equals(ContentType.ERROR);
    }

    public long getDeadline() {
        return deadline;
    }
}
