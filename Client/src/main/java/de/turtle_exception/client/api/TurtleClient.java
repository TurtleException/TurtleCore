package de.turtle_exception.client.api;

import de.turtle_exception.client.api.entities.Group;
import de.turtle_exception.client.api.entities.Ticket;
import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.entities.containers.IGroupContainer;
import de.turtle_exception.client.api.entities.containers.ITicketContainer;
import de.turtle_exception.client.api.entities.containers.IUserContainer;
import de.turtle_exception.client.api.event.EventManager;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.NetworkAdapter;
import de.turtle_exception.client.internal.Provider;
import de.turtle_exception.client.internal.net.NetClient;
import de.turtle_exception.client.internal.util.version.Version;
import net.dv8tion.jda.api.JDA;
import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

// TODO: docs
@SuppressWarnings("unused")
public interface TurtleClient extends IUserContainer, IGroupContainer, ITicketContainer {
    @NotNull Logger getLogger();

    @NotNull Version getVersion();

    @NotNull EventManager getEventManager();

    @NotNull NetworkAdapter getNetworkAdapter();

    @NotNull Provider getProvider();

    @NotNull Action<Void> invalidateCaches(boolean retrieve);

    /* - - - */

    @Override
    @NotNull List<Turtle> getTurtles();

    @Override
    @Nullable Turtle getTurtleById(long id);

    /* - - - */

    @NotNull Action<Group> retrieveGroup(long id);

    @NotNull Action<List<Group>> retrieveGroups();

    @NotNull Action<Ticket> retrieveTicket(long id);

    @NotNull Action<List<Ticket>> retrieveTickets();

    @NotNull Action<User> retrieveUser(long id);

    @NotNull Action<List<User>> retrieveUsers();

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

    default long getPing() throws UnsupportedOperationException {
        if (!(getNetworkAdapter() instanceof NetClient netClient))
            throw new UnsupportedOperationException("Only supported with NetClients");
        return netClient.getConnection().getPing();
    }

    void shutdown() throws IOException;

    void shutdownNow() throws IOException;
}
