package de.turtle_exception.client.api;

import de.turtle_exception.client.api.entities.Group;
import de.turtle_exception.client.api.entities.Ticket;
import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.entities.attribute.IGroupContainer;
import de.turtle_exception.client.api.entities.attribute.ITicketContainer;
import de.turtle_exception.client.api.entities.attribute.IUserContainer;
import de.turtle_exception.client.api.event.EventManager;
import de.turtle_exception.client.api.requests.Action;
import net.dv8tion.jda.api.JDA;
import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

// TODO: docs
@SuppressWarnings("unused")
public interface TurtleClient extends IUserContainer, IGroupContainer, ITicketContainer {
    @NotNull
    Logger getLogger();

    @NotNull EventManager getEventManager();

    @NotNull Consumer<Object> getDefaultActionSuccess();

    @NotNull Consumer<? super Throwable> getDefaultActionFailure();

    void setDefaultActionSuccess(@NotNull Consumer<Object> consumer);

    void setDefaultActionFailure(@NotNull Consumer<? super Throwable> consumer);

    /* - - - */

    @Override
    @NotNull List<Turtle> getTurtles();

    @NotNull Action<User> retrieveUser(long id);

    @NotNull Action<List<User>> retrieveUsers();

    @NotNull Action<Group> retrieveGroup(long id);

    @Override
    @Nullable Turtle getTurtleById(long id);

    @NotNull Action<List<Group>> retrieveGroups();

    @NotNull Action<Ticket> retrieveTicket(long id);

    @NotNull Action<List<Ticket>> retrieveTickets();

    /* - - - */

    @Nullable Server getSpigotServer();

    void setSpigotServer(@Nullable Server server);

    @Nullable JDA getJDA();

    void setJDA(@Nullable JDA jda);

    /* - - - */

    @Range(from = 0, to = Long.MAX_VALUE) long getDefaultTimeoutInbound();

    void setDefaultTimeoutInbound(@Range(from = 0, to = Long.MAX_VALUE) long defaultTimeoutInbound);

    @Range(from = 0, to = Long.MAX_VALUE) long getDefaultTimeoutOutbound();

    void setDefaultTimeoutOutbound(@Range(from = 0, to = Long.MAX_VALUE) long defaultTimeoutOutbound);
}
