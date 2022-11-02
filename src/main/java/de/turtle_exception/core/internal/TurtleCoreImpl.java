package de.turtle_exception.core.internal;

import de.turtle_exception.core.api.TurtleCore;
import de.turtle_exception.core.api.entitites.Group;
import de.turtle_exception.core.api.entitites.Ticket;
import de.turtle_exception.core.api.entitites.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TurtleCoreImpl implements TurtleCore {
    @Override
    public @NotNull List<Group> getGroups() {
        return null;
    }

    @Override
    public @Nullable Group getGroupById(long id) {
        return null;
    }

    @Override
    public @NotNull List<Ticket> getTickets() {
        return null;
    }

    @Override
    public @Nullable Ticket getTicketById(long id) {
        return null;
    }

    @Override
    public @NotNull List<User> getUsers() {
        return null;
    }

    @Override
    public @Nullable User getUserById(long id) {
        return null;
    }
}
