package de.turtle_exception.core.client.api;

import de.turtle_exception.core.client.api.entities.Group;
import de.turtle_exception.core.client.api.entities.User;
import de.turtle_exception.core.client.api.entities.attribute.IUserContainer;
import de.turtle_exception.core.client.api.requests.Action;
import de.turtle_exception.core.netcore.util.logging.NestedLogger;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * The heart of the TurtleClient. This functions as a root interface from which all parts of the API can be accessed.
 */
public interface TurtleClient extends IUserContainer {
    /**
     * Provides the main logger of the TurtleClient. This logger also is the parent of all
     * {@link NestedLogger NestedLoggers} that are used throughout the API.
     * @return root logger of TurtleClient.
     */
    @NotNull
    Logger getLogger();

    /**
     * The default {@link Consumer} that will be used by {@link Action} to accept successful executions.
     * @return Default Consumer for successful {@link Action Actions}.
     */
    @NotNull Consumer<Object> getDefaultActionSuccess();

    /**
     * The default {@link Consumer} that will be used by {@link Action} to accept exceptional executions.
     * @return Default Consumer for exceptional {@link Action Actions}.
     */
    @NotNull Consumer<? super Throwable> getDefaultActionFailure();

    /**
     * Sets the default {@link Consumer} that will be used by {@link Action} to accept successful executions.
     * @param consumer Default Consumer for successful {@link Action Actions}.
     */
    void setDefaultActionSuccess(@NotNull Consumer<Object> consumer);

    /**
     * Sets the default {@link Consumer} that will be used by {@link Action} to accept exceptional executions.
     * @param consumer Default Consumer for exceptional {@link Action Actions}.
     */
    void setDefaultActionFailure(@NotNull Consumer<? super Throwable> consumer);

    /**
     * Attempts to retrieve a {@link User} from the server.
     * <p>If the request is successful the new User will be cached and any old variant of it will be deleted from cache.
     * @param id The id of the requested User.
     * @return A completed Action with a User.
     */
    @NotNull Action<User> retrieveUser(long id);

    /**
     * Attempts to retrieve all available {@link User Users} from the server.
     * <p>If the request is successful the User cache will be wiped and replaced by the obtained List of Users.
     * @return A completed Action with a List of Users.
     */
    @NotNull Action<List<User>> retrieveUsers();

    /**
     * Attempts to retrieve a {@link Group} from the server.
     * <p>If the request is successful the new Group will be cached and any old variant of it will be deleted from cache.
     * @param id The id of the requested Group.
     * @return A completed Action with a Group.
     */
    @NotNull Action<Group> retrieveGroup(long id);

    /**
     * Attempts to retrieve all available {@link Group Groups} from the server.
     * <p>If the request is successful the Group cache will be wiped and replaced by the obtained List of Groups.
     * @return A completed Action with a List of Groups.
     */
    @NotNull Action<List<Group>> retrieveGroups();
}
