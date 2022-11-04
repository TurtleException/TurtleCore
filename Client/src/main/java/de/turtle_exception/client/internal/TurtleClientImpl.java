package de.turtle_exception.client.internal;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.Group;
import de.turtle_exception.client.api.entities.Ticket;
import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.event.EventManager;
import de.turtle_exception.client.api.requests.Action;
import de.turtle_exception.client.internal.data.JsonBuilder;
import de.turtle_exception.client.internal.net.NetClient;
import de.turtle_exception.client.internal.net.NetworkAdapter;
import de.turtle_exception.client.internal.net.route.Routes;
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
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
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

    private final ScheduledThreadPoolExecutor callbackExecutor;

    private @NotNull Consumer<Object>            defaultOnSuccess = o -> { };
    private @NotNull Consumer<? super Throwable> defaultOnFailure = t -> {
        // TODO
    };

    private long defaultTimeoutInbound  = TimeUnit.SECONDS.toMillis( 8);
    private long defaultTimeoutOutbound = TimeUnit.SECONDS.toMillis(16);

    private final TurtleSet<User> userCache = new TurtleSet<>();
    private final TurtleSet<Group> groupCache = new TurtleSet<>();
    private final TurtleSet<Ticket> ticketCache = new TurtleSet<>();

    /* THIRD PARTY SERVICES */
    private Server spigotServer = null;
    private JDA    jda          = null;

    public TurtleClientImpl(@Nullable String name, @NotNull Logger logger, @NotNull NetworkAdapter networkAdapter) throws IOException, LoginException {
        this.name = name;
        this.logger = logger;

        this.jsonBuilder = new JsonBuilder(this);

        this.eventManager = new EventManager();

        this.callbackExecutor = new ScheduledThreadPoolExecutor(4, (r, executor) -> logger.log(Level.WARNING, "A callback task was rejected by the executor: ", r));

        this.networkAdapter = networkAdapter;
        this.networkAdapter.setClient(this);
        this.networkAdapter.start();

        // initial requests
        this.retrieveUsers().await();
        this.retrieveGroups().await();
        this.retrieveTickets().await();
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

    @Override
    public @NotNull EventManager getEventManager() {
        return this.eventManager;
    }

    @Override
    public @NotNull NetworkAdapter getNetworkAdapter() {
        return this.networkAdapter;
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

    public @NotNull TurtleSet<User> getUserCache() {
        return userCache;
    }

    public @NotNull TurtleSet<Ticket> getTicketCache() {
        return ticketCache;
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
    public @NotNull List<User> getUsers() {
        return List.copyOf(userCache);
    }

    @Override
    public @NotNull List<Ticket> getTickets() {
        return List.copyOf(ticketCache);
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
    public @Nullable User getUserById(long id) {
        return userCache.get(id);
    }

    @Override
    public @Nullable Ticket getTicketById(long id) {
        return ticketCache.get(id);
    }

    /* - - - */

    @Override
    public @NotNull Consumer<Object> getDefaultActionSuccess() {
        return this.defaultOnSuccess;
    }

    @Override
    public @NotNull Consumer<? super Throwable> getDefaultActionFailure() {
        return this.defaultOnFailure;
    }

    @Override
    public void setDefaultActionSuccess(@NotNull Consumer<Object> consumer) {
        this.defaultOnSuccess = consumer;
    }

    @Override
    public void setDefaultActionFailure(@NotNull Consumer<? super Throwable> consumer) {
        this.defaultOnFailure = consumer;
    }

    public ScheduledThreadPoolExecutor getCallbackExecutor() {
        return callbackExecutor;
    }

    /* - - - */

    @SuppressWarnings("CodeBlock2Expr")
    @Override
    public @NotNull Action<User> retrieveUser(long id) {
        return new ActionImpl<User>(this, Routes.User.GET.compile(null, String.valueOf(id)), (message, userRequest) -> {
            return jsonBuilder.buildObject(User.class, (JsonObject) message.getRoute().content());
        }).onSuccess(user -> {
            userCache.removeById(id);
            userCache.add(user);
        });
    }

    @SuppressWarnings("CodeBlock2Expr")
    @Override
    public @NotNull Action<List<User>> retrieveUsers() {
        return new ActionImpl<List<User>>(this, Routes.User.GET_ALL.compile(null), (message, userRequest) -> {
            return jsonBuilder.buildObjects(User.class, (JsonArray) message.getRoute().content());
        }).onSuccess(l -> {
            userCache.clear();
            userCache.addAll(l);
        });
    }

    @SuppressWarnings("CodeBlock2Expr")
    @Override
    public @NotNull Action<Group> retrieveGroup(long id) {
        return new ActionImpl<Group>(this, Routes.Group.GET.compile(null, String.valueOf(id)), (message, userRequest) -> {
            return jsonBuilder.buildObject(Group.class, (JsonObject) message.getRoute().content());
        }).onSuccess(group -> {
            groupCache.removeById(id);
            groupCache.add(group);
        });
    }

    @SuppressWarnings("CodeBlock2Expr")
    @Override
    public @NotNull Action<List<Group>> retrieveGroups() {
        return new ActionImpl<List<Group>>(this, Routes.Group.GET_ALL.compile(null), (message, userRequest) -> {
            return jsonBuilder.buildObjects(Group.class, (JsonArray) message.getRoute().content());
        }).onSuccess(l -> {
            groupCache.clear();
            groupCache.addAll(l);
        });
    }

    @SuppressWarnings("CodeBlock2Expr")
    @Override
    public @NotNull Action<Ticket> retrieveTicket(long id) {
        return new ActionImpl<Ticket>(this, Routes.Ticket.GET.compile(null, String.valueOf(id)), (message, userRequest) -> {
            return jsonBuilder.buildObject(Ticket.class, (JsonObject) message.getRoute().content());
        }).onSuccess(ticket -> {
            ticketCache.removeById(id);
            ticketCache.add(ticket);
        });
    }

    @SuppressWarnings("CodeBlock2Expr")
    @Override
    public @NotNull Action<List<Ticket>> retrieveTickets() {
        return new ActionImpl<List<Ticket>>(this, Routes.Ticket.GET_ALL.compile(null), (message, userRequest) -> {
            return jsonBuilder.buildObjects(Ticket.class, (JsonArray) message.getRoute().content());
        }).onSuccess(l -> {
            ticketCache.clear();
            ticketCache.addAll(l);
        });
    }

    /* - - - */

    @Override
    public @Nullable Server getSpigotServer() {
        return this.spigotServer;
    }

    @Override
    public void setSpigotServer(@Nullable Server server) {
        this.spigotServer = server;
    }

    @Override
    public @Nullable JDA getJDA() {
        return this.jda;
    }

    @Override
    public void setJDA(@Nullable JDA jda) {
        this.jda = jda;
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
        this.networkAdapter.stop();
    }
}
