package de.turtle_exception.core.client.api;

import de.turtle_exception.core.client.api.entities.Group;
import de.turtle_exception.core.client.api.entities.User;
import de.turtle_exception.core.client.api.entities.attribute.IUserContainer;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

// TODO: docs
public interface TurtleClient extends IUserContainer {
    @NotNull
    Logger getLogger();

    @NotNull Consumer<Object> getDefaultActionSuccess();

    @NotNull Consumer<? super Throwable> getDefaultActionFailure();

    void setDefaultActionSuccess(@NotNull Consumer<Object> consumer);

    void setDefaultActionFailure(@NotNull Consumer<? super Throwable> consumer);

    @NotNull Action<User> retrieveUser(long id);

    @NotNull Action<List<User>> retrieveUsers();

    @NotNull Action<Group> retrieveGroup(long id);

    @NotNull Action<List<Group>> retrieveGroups();
}
