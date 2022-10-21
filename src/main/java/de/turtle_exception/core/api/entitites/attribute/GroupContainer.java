package de.turtle_exception.core.api.entitites.attribute;

import de.turtle_exception.core.api.entitites.Group;
import de.turtle_exception.core.api.entitites.Turtle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public interface GroupContainer extends TurtleContainer {
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
