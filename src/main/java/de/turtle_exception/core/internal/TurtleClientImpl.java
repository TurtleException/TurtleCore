package de.turtle_exception.core.internal;

import de.turtle_exception.core.api.TurtleClient;
import de.turtle_exception.core.api.entities.Group;
import de.turtle_exception.core.api.entities.User;
import de.turtle_exception.core.api.net.Action;
import de.turtle_exception.core.internal.entities.EntityBuilder;
import de.turtle_exception.core.internal.net.Route;
import de.turtle_exception.core.internal.net.action.ContentAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TurtleClientImpl extends TurtleCore implements TurtleClient {
    public TurtleClientImpl(@Nullable String name) {
        super(name);
    }

    public TurtleClientImpl() {
        this(null);
    }

    @Override
    public @NotNull Action<User> retrieveUser(long id) {
        return new ContentAction<>(this, Route.Content.User.GET, EntityBuilder::buildUser);
    }

    @Override
    public @NotNull Action<List<User>> retrieveUsers() {
        return new ContentAction<>(this, Route.Content.User.GET_ALL, EntityBuilder::buildUsers);
    }

    @Override
    public @NotNull Action<Group> retrieveGroup(long id) {
        return new ContentAction<>(this, Route.Content.Group.GET, EntityBuilder::buildGroup);
    }

    @Override
    public @NotNull Action<List<Group>> retrieveGroups() {
        return new ContentAction<>(this, Route.Content.Group.GET_ALL, EntityBuilder::buildGroups);
    }
}
