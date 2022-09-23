package de.turtle_exception.core.client.internal;

import de.turtle_exception.core.api.entities.User;
import de.turtle_exception.core.api.entities.attribute.IUserContainer;
import de.turtle_exception.core.client.internal.util.TurtleSet;
import de.turtle_exception.core.client.internal.net.DefaultRequestConsumerHolder;
import de.turtle_exception.core.client.internal.net.NetworkAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.logging.Logger;

public abstract class TurtleCore implements IUserContainer, DefaultRequestConsumerHolder {
    /** The root logger of this core */
    private final Logger logger;

    /** Name of this instance. Naming is not required, but it may be helpful when using multiple instances. */
    private final @Nullable String name;

    /** The internal server / client */
    protected NetworkAdapter networkAdapter;

    private Consumer<Object>            defaultOnSuccess = o -> { };
    private Consumer<? super Throwable> defaultOnFailure = t -> {
        // TODO
    };

    private final TurtleSet<User> userCache = new TurtleSet<>();

    protected TurtleCore(@Nullable String name) {
        this.name = name;
        this.logger = Logger.getLogger(name != null ? "CORE#" + name : "CORE");
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

    public NetworkAdapter getNetworkAdapter() {
        return networkAdapter;
    }

    public @NotNull TurtleSet<User> getUserCache() {
        return userCache;
    }

    @Override
    public @Nullable User getUserById(long id) {
        return userCache.get(id);
    }

    @Override
    public @NotNull Consumer<Object> getDefaultOnSuccess() {
        return this.defaultOnSuccess;
    }

    @Override
    public void setDefaultOnSuccess(@NotNull Consumer<Object> c) {
        this.defaultOnSuccess = c;
    }

    @Override
    public @NotNull Consumer<? super Throwable> getDefaultOnFailure() {
        return this.defaultOnFailure;
    }

    @Override
    public void setDefaultOnFailure(@NotNull Consumer<? super Throwable> c) {
        this.defaultOnFailure = c;
    }
}
