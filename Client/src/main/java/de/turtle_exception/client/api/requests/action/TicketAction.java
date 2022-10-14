package de.turtle_exception.client.api.requests.action;

import de.turtle_exception.client.api.TicketState;
import de.turtle_exception.client.api.entities.Ticket;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.requests.Action;
import org.jetbrains.annotations.NotNull;

public interface TicketAction extends Action<Ticket> {
    @NotNull TicketAction setState(byte state);

    default @NotNull TicketAction setState(@NotNull TicketState state) {
        return this.setState(state.getCode());
    }

    @NotNull TicketAction setTitle(@NotNull String title);

    @NotNull TicketAction setCategory(@NotNull String category);

    @NotNull TicketAction setDiscordChannel(long snowflake);

    @NotNull TicketAction addTag(@NotNull String tag);

    @NotNull TicketAction addUser(long id);

    default @NotNull TicketAction addUser(@NotNull User user) {
        return this.addUser(user.getId());
    }
}
