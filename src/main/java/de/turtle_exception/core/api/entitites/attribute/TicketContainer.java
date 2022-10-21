package de.turtle_exception.core.api.entitites.attribute;

import de.turtle_exception.core.api.entitites.Ticket;
import de.turtle_exception.core.api.entitites.Turtle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public interface TicketContainer extends TurtleContainer {
    @NotNull List<Ticket> getTickets();

    @Nullable Ticket getTicketById(long id);

    @Override
    default @NotNull List<Turtle> getTurtles() {
        return List.copyOf(new ArrayList<>(getTickets()));
    }

    @Override
    default @Nullable Turtle getTurtleById(long id) {
        return getTicketById(id);
    }
}
