package de.turtle_exception.client.api.entities.attribute;

import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.api.entities.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public interface IUserContainer extends ITurtleContainer {
    @NotNull List<User> getUsers();

    @Nullable User getUserById(long id);

    @Override
    default @NotNull List<Turtle> getTurtles() {
        return List.copyOf(new ArrayList<>(getUsers()));
    }

    @Override
    default @Nullable Turtle getTurtleById(long id) {
        return getUserById(id);
    }
}
