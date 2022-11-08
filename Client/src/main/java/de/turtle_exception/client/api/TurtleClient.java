package de.turtle_exception.client.api;

import com.google.gson.JsonObject;
import de.turtle_exception.client.api.entities.Group;
import de.turtle_exception.client.api.entities.Ticket;
import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.entities.attribute.IGroupContainer;
import de.turtle_exception.client.api.entities.attribute.ITicketContainer;
import de.turtle_exception.client.api.entities.attribute.IUserContainer;
import de.turtle_exception.client.api.event.EventManager;
import de.turtle_exception.client.api.request.DataAction;
import de.turtle_exception.client.internal.NetworkAdapter;
import de.turtle_exception.client.internal.Provider;
import de.turtle_exception.client.internal.data.JsonBuilder;
import de.turtle_exception.client.internal.util.version.Version;
import net.dv8tion.jda.api.JDA;
import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

// TODO: docs
@SuppressWarnings("unused")
public interface TurtleClient extends IUserContainer, IGroupContainer, ITicketContainer {
    @NotNull Logger getLogger();

    @NotNull Version getVersion();

    @NotNull JsonBuilder getJsonBuilder();

    @NotNull EventManager getEventManager();

    @NotNull NetworkAdapter getNetworkAdapter();

    @NotNull Provider getProvider();

    @NotNull Consumer<Object> getDefaultActionSuccess();

    @NotNull Consumer<? super Throwable> getDefaultActionFailure();

    void setDefaultActionSuccess(@NotNull Consumer<Object> consumer);

    void setDefaultActionFailure(@NotNull Consumer<? super Throwable> consumer);

    /* - - - */

    @Override
    @NotNull List<Turtle> getTurtles();

    @NotNull DataAction<User> retrieveUser(long id);

    @NotNull DataAction<List<User>> retrieveUsers();

    @NotNull DataAction<Group> retrieveGroup(long id);

    @Override
    @Nullable Turtle getTurtleById(long id);

    @NotNull DataAction<List<Group>> retrieveGroups();

    @NotNull DataAction<Ticket> retrieveTicket(long id);

    @NotNull DataAction<List<Ticket>> retrieveTickets();

    void updateCache(@NotNull Class<?> type, @NotNull JsonObject contentJson) throws IllegalArgumentException;

    void removeCache(@NotNull Class<?> type, @NotNull Object primary) throws IllegalArgumentException, ClassCastException;

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

    /* - - - */

    void shutdown() throws IOException;
}
