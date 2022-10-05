package de.turtle_exception.client.api.entities.attribute;

import de.turtle_exception.client.api.entities.User;
import org.jetbrains.annotations.Nullable;

public interface IUserContainer {
    @Nullable User getUserById(long id);
}
