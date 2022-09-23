package de.turtle_exception.core.internal.net.action;

import de.turtle_exception.core.api.net.Action;
import de.turtle_exception.core.internal.TurtleClientImpl;
import de.turtle_exception.core.internal.TurtleServerImpl;
import de.turtle_exception.core.internal.net.ActionImpl;
import de.turtle_exception.core.internal.net.Route;
import de.turtle_exception.core.internal.net.server.VirtualClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * An {@link Action} that returns an object. Answerable actions will wait for a response and use a {@link Function} to
 * produce an object of type T from the String response.
 * @param <T> Type of the return Object.
 */
public class AnswerableAction<T> extends ActionImpl<T> {
    private final Function<RemoteActionImpl, T> handler;

    private RemoteActionImpl response = null;

    public AnswerableAction(@NotNull TurtleClientImpl core, @NotNull Route route, Function<RemoteActionImpl, T> handler) {
        super(core, route);
        this.handler = handler;
    }

    public AnswerableAction(@NotNull TurtleServerImpl core, @NotNull Route route, @NotNull VirtualClient target, Function<RemoteActionImpl, T> handler) {
        super(core, route, target);
        this.handler = handler;
    }

    public @Nullable T handleResponse(@NotNull RemoteActionImpl response) {
        this.response = response;
        if (handler == null) return null;
        return this.handler.apply(response);
    }

    public @Nullable RemoteActionImpl getResponse() {
        return response;
    }
}
