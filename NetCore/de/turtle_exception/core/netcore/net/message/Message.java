package de.turtle_exception.core.netcore.net.message;

import de.turtle_exception.core.netcore.TurtleCore;
import de.turtle_exception.core.netcore.net.route.Route;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Message {
    protected final @NotNull TurtleCore core;

    protected final int callbackCode;
    protected final @NotNull Route route;
    protected final @NotNull String content;

    protected final long deadline;

    public boolean done      = false;
    public boolean cancelled = false;

    public Message(@NotNull TurtleCore core, int callbackCode, @NotNull Route route, @Nullable String content, long deadline) {
        this.core         = core;
        this.callbackCode = callbackCode;
        this.route        = route;
        this.content      = content != null ? content : "";
        this.deadline     = deadline;
    }

    public @NotNull TurtleCore getCore() {
        return core;
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

    public long getDeadline() {
        return deadline;
    }
}
