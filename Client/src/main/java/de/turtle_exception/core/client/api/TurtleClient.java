package de.turtle_exception.core.client.api;

import de.turtle_exception.core.client.api.entities.Group;
import de.turtle_exception.core.client.api.entities.User;
import de.turtle_exception.core.client.api.entities.attribute.IUserContainer;
import de.turtle_exception.core.client.api.net.Action;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.logging.Logger;

public interface TurtleClient extends IUserContainer {
    @NotNull
    Logger getLogger();

    @NotNull Action<User> retrieveUser(long id);

    @NotNull Action<List<User>> retrieveUsers();

    @NotNull Action<Group> retrieveGroup(long id);

    @NotNull Action<List<Group>> retrieveGroups();
}
