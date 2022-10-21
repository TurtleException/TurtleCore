package de.turtle_exception.core.api.entitites.attribute;

import de.turtle_exception.core.api.entitites.Turtle;
import de.turtle_exception.core.api.entitites.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public interface UserContainer extends TurtleContainer {
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
