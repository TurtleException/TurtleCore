package de.turtle_exception.client.api.entities.attribute;

import de.turtle_exception.client.api.entities.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IUserContainer {
    @NotNull List<User> getUsers();

    @Nullable User getUserById(long id);
}
