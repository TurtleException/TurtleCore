package de.turtle_exception.client.api.entities.containers;

import de.turtle_exception.client.api.entities.Group;
import de.turtle_exception.client.api.entities.Turtle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public interface IGroupContainer extends ITurtleContainer {
    @NotNull List<Group> getGroups();

    @Nullable Group getGroupById(long id);

    @Override
    default @NotNull List<Turtle> getTurtles() {
        return List.copyOf(new ArrayList<>(getGroups()));
    }

    @Override
    default @Nullable Turtle getTurtleById(long id) {
        return getGroupById(id);
    }
}
