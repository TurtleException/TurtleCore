package de.turtle_exception.client.internal.requests.action;

import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.Ticket;
import de.turtle_exception.client.api.requests.ActionHandler;
import de.turtle_exception.client.api.requests.action.TicketAction;
import de.turtle_exception.client.internal.ActionImpl;
import de.turtle_exception.core.net.route.Routes;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class TicketActionImpl extends ActionImpl<Ticket> implements TicketAction {
    public TicketActionImpl(@NotNull TurtleClient client, ActionHandler<Ticket> handler) {
        super(client, Routes.Ticket.CREATE, handler);
    }

    @Override
    public TicketActionImpl onSuccess(Consumer<? super Ticket> consumer) {
        super.onSuccess(consumer);
        return this;
    }
}
