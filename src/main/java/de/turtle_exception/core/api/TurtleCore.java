package de.turtle_exception.core.api;

import de.turtle_exception.core.api.entitites.Turtle;
import de.turtle_exception.core.api.entitites.attribute.GroupContainer;
import de.turtle_exception.core.api.entitites.attribute.TicketContainer;
import de.turtle_exception.core.api.entitites.attribute.TurtleContainer;
import de.turtle_exception.core.api.entitites.attribute.UserContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public interface TurtleCore extends TurtleContainer, GroupContainer, TicketContainer, UserContainer {
    @Override
    default @NotNull List<Turtle> getTurtles() {
        ArrayList<Turtle> list = new ArrayList<>();
        list.addAll(this.getGroups());
        list.addAll(this.getTickets());
        list.addAll(this.getUsers());
        return List.copyOf(list);
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    default @Nullable Turtle getTurtleById(long id) {
        Turtle turtle;

        turtle = this.getGroupById(id);
        if (turtle != null) return turtle;

        turtle = this.getTicketById(id);
        if (turtle != null) return turtle;

        turtle = this.getUserById(id);
        if (turtle != null) return turtle;

        return null;
    }
}
