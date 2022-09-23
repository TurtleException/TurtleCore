package de.turtle_exception.core.api;

import de.turtle_exception.core.api.entities.Group;
import de.turtle_exception.core.api.entities.User;
import de.turtle_exception.core.api.entities.attribute.IUserContainer;
import de.turtle_exception.core.api.net.Action;
import de.turtle_exception.core.internal.net.DefaultRequestConsumerHolder;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.logging.Logger;

public interface TurtleClient extends IUserContainer, DefaultRequestConsumerHolder {
    @NotNull Logger getLogger();

    @NotNull Action<User> retrieveUser(long id);

    @NotNull Action<List<User>> retrieveUsers();

    @NotNull Action<Group> retrieveGroup(long id);

    @NotNull Action<List<Group>> retrieveGroups();
}
