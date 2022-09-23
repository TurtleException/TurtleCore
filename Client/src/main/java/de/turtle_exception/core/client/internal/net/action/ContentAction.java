package de.turtle_exception.core.client.internal.net.action;

import de.turtle_exception.core.client.internal.TurtleClientImpl;
import de.turtle_exception.core.client.internal.TurtleServerImpl;
import de.turtle_exception.core.client.internal.net.Route;
import de.turtle_exception.core.client.internal.net.server.VirtualClient;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * An {@link AnswerableAction} that transforms its response content into an object of type T with a provided
 * {@link Function}.
 * @param <T> Type of the resulting object.
 */
public class ContentAction<T> extends AnswerableAction<T> {
    public ContentAction(@NotNull TurtleClientImpl core, @NotNull Route route, Function<String, T> handler) {
        super(core, route, action -> handler.apply(action.getContent()));
    }

    public ContentAction(@NotNull TurtleServerImpl core, @NotNull Route route, @NotNull VirtualClient target, Function<String, T> handler) {
        super(core, route, target, action -> handler.apply(action.getContent()));
    }
}
