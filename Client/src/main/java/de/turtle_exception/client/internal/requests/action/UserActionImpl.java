package de.turtle_exception.client.internal.requests.action;

import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.requests.ActionHandler;
import de.turtle_exception.client.api.requests.action.UserAction;
import de.turtle_exception.client.internal.ActionImpl;
import de.turtle_exception.core.net.route.Routes;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class UserActionImpl extends ActionImpl<User> implements UserAction {
    public UserActionImpl(@NotNull TurtleClient client, ActionHandler<User> handler) {
        super(client, Routes.User.CREATE, handler);
    }

    @Override
    public UserActionImpl onSuccess(Consumer<? super User> consumer) {
        super.onSuccess(consumer);
        return this;
    }
}
