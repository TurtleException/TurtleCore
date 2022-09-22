package de.turtle_exception.core.internal;

import de.turtle_exception.core.api.entities.User;
import de.turtle_exception.core.api.entities.attribute.IUserContainer;
import de.turtle_exception.core.internal.util.TurtleSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Logger;

public abstract class TurtleCore implements IUserContainer {
    /** The root logger of this core */
    private final Logger logger;

    /** Name of this instance. Naming is not required, but it may be helpful when using multiple instances. */
    private final @Nullable String name;

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

    public @NotNull TurtleSet<User> getUserCache() {
        return userCache;
    }

    @Override
    public @Nullable User getUserById(long id) {
        return userCache.get(id);
    }
}
