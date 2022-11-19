package de.turtle_exception.client.internal;

import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.Group;
import de.turtle_exception.client.api.entities.Ticket;
import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.event.EventManager;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.data.JsonBuilder;
import de.turtle_exception.client.internal.entities.GroupImpl;
import de.turtle_exception.client.internal.entities.TicketImpl;
import de.turtle_exception.client.internal.entities.TurtleImpl;
import de.turtle_exception.client.internal.entities.UserImpl;
import de.turtle_exception.client.internal.event.UpdateHelper;
import de.turtle_exception.client.internal.net.NetClient;
import de.turtle_exception.client.internal.net.NetworkProvider;
import de.turtle_exception.client.internal.request.actions.SimpleAction;
import de.turtle_exception.client.internal.util.TurtleSet;
import de.turtle_exception.client.internal.util.version.IllegalVersionException;
import de.turtle_exception.client.internal.util.version.Version;
import net.dv8tion.jda.api.JDA;
import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TurtleClientImpl implements TurtleClient {
    public static final Version VERSION;
    static {
        VERSION = Version.retrieveFromResources(TurtleClient.class);

        // this can only happen with faulty code
        if (VERSION == null)
            throw new IllegalVersionException("Version may not be null.");
    }

    /** The root logger of this core */
    private final Logger logger;

    /** Name of this instance. Naming is not required, but it may be helpful when using multiple instances. */
    private final @Nullable String name;

    private final JsonBuilder jsonBuilder;
    private final EventManager eventManager;
    /** The internal network part of the client */
    private final NetworkAdapter networkAdapter;
    private final Provider provider;

    private long defaultTimeoutInbound  = TimeUnit.SECONDS.toMillis( 8);
    private long defaultTimeoutOutbound = TimeUnit.SECONDS.toMillis(16);

    private final TurtleSet<Group> groupCache = new TurtleSet<>();
    private final TurtleSet<Ticket> ticketCache = new TurtleSet<>();
    private final TurtleSet<User> userCache = new TurtleSet<>();

    /* THIRD PARTY SERVICES */
    private Server spigotServer = null;
    private JDA    jda          = null;

    public TurtleClientImpl(@Nullable String name, @NotNull Logger logger, @NotNull NetworkAdapter networkAdapter, @NotNull Provider provider) throws IOException, LoginException, TimeoutException {
        this.name = name;
        this.logger = logger;
        this.logger.log(Level.INFO, "Hello there  (Starting...)");

        this.logger.log(Level.FINE, "Initializing JsonBuilder.");
        this.jsonBuilder = new JsonBuilder(this);

        this.logger.log(Level.FINE, "Initializing EventManager.");
        this.eventManager = new EventManager(this);

        this.logger.log(Level.FINE, "Starting NetworkAdapter (" + networkAdapter.getClass().getSimpleName() + ")");
        this.networkAdapter = networkAdapter;
        this.networkAdapter.setClient(this);
        this.networkAdapter.start();

        this.logger.log(Level.FINE, "Starting Provider (" + provider.getClass().getSimpleName() + ")");
        this.provider = provider;
        this.provider.setClient(this);

        if (this.provider instanceof NetworkProvider netProvider) {
            if (networkAdapter instanceof NetClient netClient)
                netProvider.setConnection(netClient.getConnection());
            else
                throw new LoginException("NetworkProvider is not supported without NetClient");
        }

        this.logger.log(Level.FINE, "Dispatching initial requests...");

        /*
        * While entities are normally ordered alphabetically, the initial requests have to be ordered like this because
        * the existence of User objects is essential to parsing other objects, that reference users, later.
        */
        // initial requests
        this.retrieveUsers().complete();
        this.retrieveGroups().complete();
        this.retrieveTickets().complete();

        this.logger.log(Level.FINE, "OK!");

        this.logger.log(Level.INFO, "General Kenobi O_o  (Startup done)");
    }

    /**
     * Provides the root logger of this instance.
     * @return Instance root logger.
     */
    @Override
    public @NotNull Logger getLogger() {
        return logger;
    }

    @Override
    public @NotNull Version getVersion() {
        return VERSION;
    }

    public @NotNull JsonBuilder getJsonBuilder() {
        return jsonBuilder;
    }

    @Override
    public @NotNull EventManager getEventManager() {
        return this.eventManager;
    }

    @Override
    public @NotNull NetworkAdapter getNetworkAdapter() {
        return this.networkAdapter;
    }

    @Override
    public @NotNull Provider getProvider() {
        return this.provider;
    }

    // note: this will create a deadlock when using a DatabaseProvider
    @Override
    public @NotNull Action<Void> invalidateCaches(boolean retrieve) {
        return new SimpleAction<>(provider, () -> {
            this.groupCache.clear();
            this.ticketCache.clear();
            this.userCache.clear();

            if (retrieve) {
                // dependency order
                this.retrieveUsers().complete();
                this.retrieveGroups().complete();
                this.retrieveTickets().complete();
            }
            return null;
        });
    }

    /**
     * Provides the name of this instance. The name can be set during initialization depending on the implementation.
     * @return Instance name.
     */
    public @Nullable String getName() {
        return name;
    }

    public @NotNull TurtleSet<Group> getGroupCache() {
        return groupCache;
    }

    public @NotNull TurtleSet<Ticket> getTicketCache() {
        return ticketCache;
    }

    public @NotNull TurtleSet<User> getUserCache() {
        return userCache;
    }

    @Override
    public @NotNull List<Turtle> getTurtles() {
        ArrayList<Turtle> list = new ArrayList<>();
        list.addAll(groupCache);
        list.addAll(userCache);
        list.addAll(ticketCache);
        return list;
    }

    @Override
    public @NotNull List<Group> getGroups() {
        return List.copyOf(groupCache);
    }

    @Override
    public @NotNull List<Ticket> getTickets() {
        return List.copyOf(ticketCache);
    }

    @Override
    public @NotNull List<User> getUsers() {
        return List.copyOf(userCache);
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    public @Nullable Turtle getTurtleById(long id) {
        Group group = groupCache.get(id);
        if (group != null) return group;
        User user = userCache.get(id);
        if (user != null) return user;
        Ticket ticket = ticketCache.get(id);
        if (ticket != null) return ticket;
        return null;
    }

    @Override
    public @Nullable Group getGroupById(long id) {
        return groupCache.get(id);
    }

    @Override
    public @Nullable Ticket getTicketById(long id) {
        return ticketCache.get(id);
    }

    @Override
    public @Nullable User getUserById(long id) {
        return userCache.get(id);
    }

    /* - - - */

    @Override
    public @NotNull Action<Group> retrieveGroup(long id) {
        return provider.get(Group.class, id).andThenParse(Group.class).onSuccess(group -> {
            groupCache.removeById(id);
            groupCache.add(group);
        });
    }

    @Override
    public @NotNull Action<List<Group>> retrieveGroups() {
        return provider.get(Group.class).andThenParseList(Group.class).onSuccess(l -> {
            groupCache.clear();
            groupCache.addAll(l);
        });
    }

    @Override
    public @NotNull Action<Ticket> retrieveTicket(long id) {
        return provider.get(Ticket.class, id).andThenParse(Ticket.class).onSuccess(ticket -> {
            ticketCache.removeById(id);
            ticketCache.add(ticket);
        });
    }

    @Override
    public @NotNull Action<List<Ticket>> retrieveTickets() {
        return provider.get(Ticket.class).andThenParseList(Ticket.class).onSuccess(l -> {
            ticketCache.clear();
            ticketCache.addAll(l);
        });
    }

    @Override
    public @NotNull Action<User> retrieveUser(long id) {
        return provider.get(User.class, id).andThenParse(User.class).onSuccess(user -> {
            userCache.removeById(id);
            userCache.add(user);
        });
    }

    @Override
    public @NotNull Action<List<User>> retrieveUsers() {
        return provider.get(User.class).andThenParseList(User.class).onSuccess(l -> {
            userCache.clear();
            userCache.addAll(l);
        });
    }

    /* - - - */

    public <T extends Turtle> Turtle updateTurtle(@NotNull Class<T> type, @NotNull JsonObject content) {
        long id = content.get("id").getAsLong();
        T turtle = this.getTurtleById(id, type);

        if (turtle == null) {
            // create new object
            turtle = this.getJsonBuilder().buildObject(type, content);
            UpdateHelper.ofCreateTurtle(turtle);
        } else {
            // update object
            if (!(turtle instanceof TurtleImpl turtleImpl))
                throw new AssertionError("Turtle implementation must extend TurtleImpl");

            try {
                turtle = type.cast(turtleImpl.handleUpdate(content));
            } catch (Exception e) {
                throw new AssertionError("Turtle implementation error", e);
            }
        }

        this.updateCache(turtle);
        return turtle;
    }

    public void removeTurtle(@NotNull Turtle turtle) {
        this.removeCache(turtle.getClass(), turtle.getId());
        UpdateHelper.ofDeleteTurtle(turtle);
    }

    private void updateCache(@NotNull Turtle turtle) throws IllegalArgumentException {
        if (turtle instanceof GroupImpl group)
            groupCache.put(group);
        if (turtle instanceof TicketImpl ticket)
            ticketCache.put(ticket);
        if (turtle instanceof UserImpl user)
            userCache.put(user);

        this.logger.log(Level.FINER, "Updated cache for turtle " + turtle.getId() + ".");
    }

    public void removeCache(@NotNull Class<? extends Turtle> type, long id) throws IllegalArgumentException, ClassCastException {
        if (Group.class.isAssignableFrom(type))
            groupCache.removeById(id);
        if (Ticket.class.isAssignableFrom(type))
            ticketCache.removeById(id);
        if (User.class.isAssignableFrom(type))
            userCache.removeById(id);

        this.logger.log(Level.FINER, "Removed turtle " + id + " from cache.");
    }

    /* - - - */

    @Override
    public @Nullable Server getSpigotServer() {
        return this.spigotServer;
    }

    @Override
    public void setSpigotServer(@Nullable Server server) {
        this.spigotServer = server;
        this.logger.log(Level.INFO, "Registered Spigot server instance: " + server + ".");
    }

    @Override
    public @Nullable JDA getJDA() {
        return this.jda;
    }

    @Override
    public void setJDA(@Nullable JDA jda) {
        this.jda = jda;
        this.logger.log(Level.INFO, "Registered JDA instance: " + jda + ".");
    }

    /* - - - */

    @Override
    public @Range(from = 0, to = Long.MAX_VALUE) long getDefaultTimeoutInbound() {
        return defaultTimeoutInbound;
    }

    @Override
    public void setDefaultTimeoutInbound(@Range(from = 0, to = Long.MAX_VALUE) long defaultTimeoutInbound) {
        this.defaultTimeoutInbound = defaultTimeoutInbound;
    }

    @Override
    public @Range(from = 0, to = Long.MAX_VALUE) long getDefaultTimeoutOutbound() {
        return defaultTimeoutOutbound;
    }

    @Override
    public void setDefaultTimeoutOutbound(@Range(from = 0, to = Long.MAX_VALUE) long defaultTimeoutOutbound) {
        this.defaultTimeoutOutbound = defaultTimeoutOutbound;
    }

    /* - - - */

    @Override
    public void shutdown() throws IOException {
        logger.log(Level.INFO, "Shutting down...");
        this.networkAdapter.shutdown();
        this.provider.shutdown();
        logger.log(Level.INFO, "OK bye.");
    }
}
