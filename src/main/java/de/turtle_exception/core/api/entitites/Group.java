package de.turtle_exception.core.api.entitites;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Group extends Turtle {
    @NotNull String getName();

    @NotNull List<User> getUsers();
}
