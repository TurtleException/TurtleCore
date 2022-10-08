package de.turtle_exception.client.api.entities.attribute;

import de.turtle_exception.client.api.entities.Ticket;
import de.turtle_exception.client.api.entities.Turtle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public interface ITicketContainer extends ITurtleContainer {
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
