package de.turtle_exception.core.internal;

import de.turtle_exception.core.api.TurtleCore;
import de.turtle_exception.core.api.entitites.Group;
import de.turtle_exception.core.api.entitites.Ticket;
import de.turtle_exception.core.api.entitites.User;
import de.turtle_exception.core.internal.data.Provider;
import de.turtle_exception.core.internal.net.TurtleServer;
import de.turtle_exception.core.internal.util.TurtleSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TurtleCoreImpl implements TurtleCore {
    private final TurtleSet<Group> groupCache = new TurtleSet<>();
    private final TurtleSet<Ticket> ticketCache = new TurtleSet<>();
    private final TurtleSet<User> userCache = new TurtleSet<>();

    private TurtleServer server = null;
    private @NotNull Provider provider;

    public TurtleCoreImpl() {
        // TODO
    }

    public void setServer(/* TODO */) {
        this.server = new TurtleServer();
    }

    public void setProvider(@NotNull Provider provider) {
        this.provider = provider;
    }

    /* - API methods - */

    @Override
    public @NotNull List<Group> getGroups() {
        return List.copyOf(this.groupCache);
    }

    @Override
    public @Nullable Group getGroupById(long id) {
        return this.groupCache.get(id);
    }

    @Override
    public @NotNull List<Ticket> getTickets() {
        return List.copyOf(this.ticketCache);
    }

    @Override
    public @Nullable Ticket getTicketById(long id) {
        return this.ticketCache.get(id);
    }

    @Override
    public @NotNull List<User> getUsers() {
        return List.copyOf(this.userCache);
    }

    @Override
    public @Nullable User getUserById(long id) {
        return this.userCache.get(id);
    }
}
