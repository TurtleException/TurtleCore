package de.turtle_exception.client.internal.requests.action;

import com.google.common.collect.Sets;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.Group;
import de.turtle_exception.client.api.requests.ActionHandler;
import de.turtle_exception.client.api.requests.action.GroupAction;
import de.turtle_exception.client.internal.ActionImpl;
import de.turtle_exception.core.net.route.Routes;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Consumer;

public class GroupActionImpl extends ActionImpl<Group> implements GroupAction {
    private String name;

    private final Set<Long> users = Sets.newConcurrentHashSet();

    public GroupActionImpl(@NotNull TurtleClient client, ActionHandler<Group> handler) {
        super(client, Routes.Group.CREATE, handler);
    }

    @Override
    public GroupActionImpl onSuccess(Consumer<? super Group> consumer) {
        super.onSuccess(consumer);
        return this;
    }

    /* - - - */

    @Override
    public @NotNull GroupAction setName(@NotNull String name) {
        this.name = name;
        return this;
    }

    @Override
    public @NotNull GroupAction addUser(long id) {
        this.users.add(id);
        return this;
    }
}
