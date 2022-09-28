package de.turtle_exception.core.client.internal;

import de.turtle_exception.core.client.api.requests.Action;
import de.turtle_exception.core.client.api.TurtleClient;
import de.turtle_exception.core.client.api.entities.Group;
import de.turtle_exception.core.client.api.entities.User;
import de.turtle_exception.core.client.internal.net.client.InternalClient;
import de.turtle_exception.core.client.internal.util.TurtleSet;
import de.turtle_exception.core.netcore.TurtleCore;
import de.turtle_exception.core.netcore.net.NetworkAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class TurtleClientImpl extends TurtleCore implements TurtleClient {
    /** The root logger of this core */
    private final Logger logger;

    /** Name of this instance. Naming is not required, but it may be helpful when using multiple instances. */
    private final @Nullable String name;

    /** The internal network part of the client */
    protected InternalClient netClient;

    private @NotNull Consumer<Object>            defaultOnSuccess = o -> { };
    private @NotNull Consumer<? super Throwable> defaultOnFailure = t -> {
        // TODO
    };

    private final TurtleSet<User> userCache = new TurtleSet<>();

    protected TurtleClientImpl(@Nullable String name) {
        this.name = name;
        this.logger = Logger.getLogger(name != null ? "CLIENT#" + name : "CLIENT");

        // TODO: init netClient
    }

    /**
     * Provides the root logger of this instance.
     * @return Instance root logger.
     */
    public @NotNull Logger getLogger() {
        return logger;
    }

    /**
     * Provides the name of this instance. The name can be set during initialization depending on the implementation.
     * @return Instance name.
     */
    public @Nullable String getName() {
        return name;
    }

    public InternalClient getNetClient() {
        return netClient;
    }

    public @NotNull TurtleSet<User> getUserCache() {
        return userCache;
    }

    @Override
    public @Nullable User getUserById(long id) {
        return userCache.get(id);
    }

    /* - - - */

    @Override
    public @NotNull Consumer<Object> getDefaultActionSuccess() {
        return this.defaultOnSuccess;
    }

    @Override
    public @NotNull Consumer<? super Throwable> getDefaultActionFailure() {
        return this.defaultOnFailure;
    }

    @Override
    public void setDefaultActionSuccess(@NotNull Consumer<Object> consumer) {
        this.defaultOnSuccess = consumer;
    }

    @Override
    public void setDefaultActionFailure(@NotNull Consumer<? super Throwable> consumer) {
        this.defaultOnFailure = consumer;
    }

    @Override
    public @NotNull Action<User> retrieveUser(long id) {
        // TODO
    }

    @Override
    public @NotNull Action<List<User>> retrieveUsers() {
        // TODO
    }

    @Override
    public @NotNull Action<Group> retrieveGroup(long id) {
        // TODO
    }

    @Override
    public @NotNull Action<List<Group>> retrieveGroups() {
        // TODO
    }
}
