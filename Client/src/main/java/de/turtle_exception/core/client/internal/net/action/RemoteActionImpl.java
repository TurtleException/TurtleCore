package de.turtle_exception.core.client.internal.net.action;

import de.turtle_exception.core.client.internal.TurtleClientImpl;
import de.turtle_exception.core.client.internal.TurtleServerImpl;
import de.turtle_exception.core.client.internal.net.Route;
import de.turtle_exception.core.client.internal.net.server.VirtualClient;
import de.turtle_exception.core.client.internal.net.ActionImpl;
import org.jetbrains.annotations.NotNull;

/**
 * An incoming action that should be answered by the local application.
 */
public class RemoteActionImpl extends ActionImpl<Void> {
    private final String content;

    public RemoteActionImpl(@NotNull TurtleClientImpl core, @NotNull Route route, @NotNull String content) {
        super(core, route);
        this.content = content;
    }

    public RemoteActionImpl(@NotNull TurtleServerImpl core, @NotNull Route route, @NotNull String content, @NotNull VirtualClient target) {
        super(core, route, target);
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
