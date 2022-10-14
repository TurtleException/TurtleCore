package de.turtle_exception.client.internal.requests.action;

import com.google.common.collect.Sets;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.Ticket;
import de.turtle_exception.client.api.requests.ActionHandler;
import de.turtle_exception.client.api.requests.action.TicketAction;
import de.turtle_exception.client.internal.ActionImpl;
import de.turtle_exception.core.net.route.Routes;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Consumer;

public class TicketActionImpl extends ActionImpl<Ticket> implements TicketAction {
    private Byte   state;
    private String title;
    private String category;
    private Long   discordChannel;

    private final Set<String> tags = Sets.newConcurrentHashSet();
    private final Set<Long>  users = Sets.newConcurrentHashSet();

    public TicketActionImpl(@NotNull TurtleClient client, ActionHandler<Ticket> handler) {
        super(client, Routes.Ticket.CREATE, handler);
    }

    @Override
    public TicketActionImpl onSuccess(Consumer<? super Ticket> consumer) {
        super.onSuccess(consumer);
        return this;
    }

    /* - - - */

    @Override
    public @NotNull TicketAction setState(byte state) {
        this.state = state;
        return this;
    }

    @Override
    public @NotNull TicketAction setTitle(@NotNull String title) {
        this.title = title;
        return this;
    }

    @Override
    public @NotNull TicketAction setCategory(@NotNull String category) {
        this.category = category;
        return this;
    }

    @Override
    public @NotNull TicketAction setDiscordChannel(long snowflake) {
        this.discordChannel = snowflake;
        return this;
    }

    @Override
    public @NotNull TicketAction addTag(@NotNull String tag) {
        this.tags.add(tag);
        return this;
    }

    @Override
    public @NotNull TicketAction addUser(long id) {
        this.users.add(id);
        return this;
    }
}
