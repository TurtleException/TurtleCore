package de.turtle_exception.core.api;

import de.turtle_exception.core.api.entities.attribute.IUserContainer;
import de.turtle_exception.core.internal.net.DefaultRequestConsumerHolder;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public interface TurtleServer extends IUserContainer, DefaultRequestConsumerHolder {
    @NotNull Logger getLogger();
}
