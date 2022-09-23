package de.turtle_exception.core.client.internal.net.action;

import de.turtle_exception.core.api.net.Action;
import de.turtle_exception.core.client.internal.TurtleClientImpl;
import de.turtle_exception.core.client.internal.TurtleServerImpl;
import de.turtle_exception.core.client.internal.net.Route;
import de.turtle_exception.core.client.internal.net.server.VirtualClient;
import de.turtle_exception.core.client.internal.net.ActionImpl;
import org.jetbrains.annotations.NotNull;

/**
 * An {@link Action} that does not return an object. Void actions will not wait for a response.
 * @see AnswerableAction
 */
public class VoidAction extends ActionImpl<Void> {
    public VoidAction(@NotNull TurtleClientImpl core, @NotNull Route route) {
        super(core, route);
    }

    public VoidAction(@NotNull TurtleServerImpl core, @NotNull Route route, @NotNull VirtualClient target) {
        super(core, route, target);
    }
}
