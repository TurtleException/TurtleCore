package de.turtle_exception.client.internal;

import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.*;
import de.turtle_exception.client.api.entities.messages.DiscordChannel;
import de.turtle_exception.client.api.entities.messages.MinecraftChannel;
import de.turtle_exception.client.api.entities.messages.SyncChannel;
import de.turtle_exception.client.api.entities.messages.SyncMessage;
import de.turtle_exception.client.api.event.EventManager;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.api.request.entities.*;
import de.turtle_exception.client.api.request.entities.messages.DiscordChannelAction;
import de.turtle_exception.client.api.request.entities.messages.MinecraftChannelAction;
import de.turtle_exception.client.api.request.entities.messages.SyncChannelAction;
import de.turtle_exception.client.api.request.entities.messages.SyncMessageAction;
import de.turtle_exception.client.internal.data.ResourceBuilder;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.internal.entities.TurtleImpl;
import de.turtle_exception.client.internal.event.UpdateHelper;
import de.turtle_exception.client.internal.net.NetClient;
import de.turtle_exception.client.internal.net.NetworkProvider;
import de.turtle_exception.client.internal.request.actions.SimpleAction;
import de.turtle_exception.client.internal.request.actions.entities.*;
import de.turtle_exception.client.internal.request.actions.entities.messages.DiscordChannelActionImpl;
import de.turtle_exception.client.internal.request.actions.entities.messages.MinecraftChannelActionImpl;
import de.turtle_exception.client.internal.request.actions.entities.messages.SyncChannelActionImpl;
import de.turtle_exception.client.internal.request.actions.entities.messages.SyncMessageActionImpl;
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

    private final ResourceBuilder resourceBuilder;
    private final EventManager eventManager;
    /** The internal network part of the client */
    private final NetworkAdapter networkAdapter;
    private final Provider provider;

    private long defaultTimeoutInbound  = TimeUnit.SECONDS.toMillis( 8);
    private long defaultTimeoutOutbound = TimeUnit.SECONDS.toMillis(16);

    private final TurtleSet<Turtle> cache = new TurtleSet<>();

    /* THIRD PARTY SERVICES */
    private Server spigotServer = null;
    private JDA    jda          = null;

    public TurtleClientImpl(@Nullable String name, @NotNull Logger logger, @NotNull NetworkAdapter networkAdapter, @NotNull Provider provider, boolean autoFillCache) throws IOException, LoginException, TimeoutException, ProviderException {
        this.name = name;
        this.logger = logger;
        this.logger.log(Level.INFO, "Hello there  (Starting...)");

        this.logger.log(Level.FINE, "Initializing ResourceBuilder.");
        this.resourceBuilder = new ResourceBuilder(this);

        this.logger.log(Level.FINE, "Initializing EventManager.");
        this.eventManager = new EventManager(this);

        this.logger.log(Level.FINE, "Starting NetworkAdapter (" + networkAdapter.getClass().getSimpleName() + ")");
        this.networkAdapter = networkAdapter;
        this.networkAdapter.setClient(this);
        this.networkAdapter.start();

        this.logger.log(Level.FINE, "Starting Provider (" + provider.getClass().getSimpleName() + ")");
        this.provider = provider;
        this.provider.setClient(this);
        this.provider.start();

        if (this.provider instanceof NetworkProvider netProvider) {
            if (networkAdapter instanceof NetClient netClient)
                netProvider.setConnection(netClient.getConnection());
            else
                throw new LoginException("NetworkProvider is not supported without NetClient");
        }

        if (autoFillCache) {
            this.logger.log(Level.FINE, "Dispatching initial requests...");
            this.doInvalidateCaches(true);
            this.logger.log(Level.FINE, "OK!");
        }

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

    public @NotNull ResourceBuilder getResourceBuilder() {
        return resourceBuilder;
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
            this.doInvalidateCaches(retrieve);
            return null;
        });
    }

    private synchronized void doInvalidateCaches(boolean retrieve) {
        this.cache.clear();

        if (retrieve) {
            /*
             * While entities are normally ordered alphabetically, these requests have to be ordered like this because
             * the existence of some objects is essential to parsing other resources that reference them later.
             */
            this.retrieveUsers().complete(); // depends on nothing
            this.retrieveGroups().complete(); // depends on User
            this.retrieveTickets().complete(); // depends on User
            this.retrieveProjects().complete(); // depends on User
            this.retrieveDiscordChannels().complete(); // depends on nothing
            this.retrieveMinecraftChannels().complete(); // depends on nothing
            this.retrieveChannels().complete(); // depends on DiscordChannel & MinecraftChannel
        }
    }

    /**
     * Provides the name of this instance. The name can be set during initialization depending on the implementation.
     * @return Instance name.
     */
    public @Nullable String getName() {
        return name;
    }

    @Override
    public @NotNull List<Turtle> getTurtles() {
        return List.copyOf(this.cache);
    }

    @Override
    public @Nullable Turtle getTurtleById(long id) {
        return this.cache.get(id);
    }

    /* - - - */

    @Override
    public @NotNull Action<Group> retrieveGroup(long id) {
        return provider.get(Group.class, id).andThenParse(Group.class).onSuccess(group -> {
            cache.removeById(id);
            cache.add(group);
        });
    }

    @Override
    public @NotNull Action<List<Group>> retrieveGroups() {
        return provider.get(Group.class).andThenParseList(Group.class).onSuccess(l -> {
            cache.removeAll(Group.class);
            cache.addAll(l);
        });
    }

    @Override
    public @NotNull Action<JsonResource> retrieveJsonResource(long id) {
        return provider.get(JsonResource.class, id).andThenParse(JsonResource.class).onSuccess(jsonResource -> {
            cache.removeById(id);
            if (!jsonResource.isEphemeral())
                cache.add(jsonResource);
        });
    }

    @Override
    public @NotNull Action<Project> retrieveProject(long id) {
        return provider.get(Project.class, id).andThenParse(Project.class).onSuccess(project -> {
            cache.removeById(id);
            cache.add(project);
        });
    }

    @Override
    public @NotNull Action<List<Project>> retrieveProjects() {
        return provider.get(Project.class).andThenParseList(Project.class).onSuccess(l -> {
            cache.removeAll(Project.class);
            cache.addAll(l);
        });
    }

    @Override
    public @NotNull Action<Ticket> retrieveTicket(long id) {
        return provider.get(Ticket.class, id).andThenParse(Ticket.class).onSuccess(ticket -> {
            cache.removeById(id);
            cache.add(ticket);
        });
    }

    @Override
    public @NotNull Action<List<Ticket>> retrieveTickets() {
        return provider.get(Ticket.class).andThenParseList(Ticket.class).onSuccess(l -> {
            cache.removeAll(Ticket.class);
            cache.addAll(l);
        });
    }

    @Override
    public @NotNull Action<User> retrieveUser(long id) {
        return provider.get(User.class, id).andThenParse(User.class).onSuccess(user -> {
            cache.removeById(id);
            cache.add(user);
        });
    }

    @Override
    public @NotNull Action<List<User>> retrieveUsers() {
        return provider.get(User.class).andThenParseList(User.class).onSuccess(l -> {
            cache.removeAll(User.class);
            cache.addAll(l);
        });
    }

    // MESSAGES

    @Override
    public @NotNull Action<DiscordChannel> retrieveDiscordChannel(long id) {
        return provider.get(DiscordChannel.class, id).andThenParse(DiscordChannel.class).onSuccess(channel -> {
            cache.removeById(id);
            cache.add(channel);
        });
    }

    @Override
    public @NotNull Action<List<DiscordChannel>> retrieveDiscordChannels() {
        return provider.get(DiscordChannel.class).andThenParseList(DiscordChannel.class).onSuccess(l -> {
            cache.removeAll(DiscordChannel.class);
            cache.addAll(l);
        });
    }

    @Override
    public @NotNull Action<MinecraftChannel> retrieveMinecraftChannel(long id) {
        return provider.get(MinecraftChannel.class, id).andThenParse(MinecraftChannel.class).onSuccess(channel -> {
            cache.removeById(id);
            cache.add(channel);
        });
    }

    @Override
    public @NotNull Action<List<MinecraftChannel>> retrieveMinecraftChannels() {
        return provider.get(MinecraftChannel.class).andThenParseList(MinecraftChannel.class).onSuccess(l -> {
            cache.removeAll(MinecraftChannel.class);
            cache.addAll(l);
        });
    }

    @Override
    public @NotNull Action<SyncChannel> retrieveChannel(long id) {
        return provider.get(SyncChannel.class, id).andThenParse(SyncChannel.class).onSuccess(channel -> {
            cache.removeById(id);
            cache.add(channel);
        });
    }

    @Override
    public @NotNull Action<List<SyncChannel>> retrieveChannels() {
        return provider.get(SyncChannel.class).andThenParseList(SyncChannel.class).onSuccess(l -> {
            cache.removeAll(SyncChannel.class);
            cache.addAll(l);
        });
    }

    @Override
    public @NotNull Action<SyncMessage> retrieveMessage(long id) {
        return provider.get(SyncMessage.class, id).andThenParse(SyncMessage.class).onSuccess(message -> {
            cache.removeById(id);
            cache.add(message);
        });
    }

    /* - - - */

    @Override
    public @NotNull GroupAction createGroup() {
        return new GroupActionImpl(this.provider);
    }

    @Override
    public @NotNull JsonResourceAction createJsonResource() {
        return new JsonResourceActionImpl(this.provider);
    }

    @Override
    public @NotNull ProjectAction createProject() {
        return new ProjectActionImpl(this.provider);
    }

    @Override
    public @NotNull TicketAction createTicket() {
        return new TicketActionImpl(this.provider);
    }

    @Override
    public @NotNull UserAction createUser() {
        return new UserActionImpl(this.provider);
    }

    // MESSAGES

    @Override
    public @NotNull DiscordChannelAction createDiscordChannel() {
        return new DiscordChannelActionImpl(this.provider);
    }

    @Override
    public @NotNull MinecraftChannelAction createMinecraftChannel() {
        return new MinecraftChannelActionImpl(this.provider);
    }

    @Override
    public @NotNull SyncChannelAction createChannel() {
        return new SyncChannelActionImpl(this.provider);
    }

    @Override
    public @NotNull SyncMessageAction createMessage() {
        return new SyncMessageActionImpl(this.provider);
    }

    /* - - - */

    public <T extends Turtle> Turtle updateTurtle(@NotNull Class<T> type, @NotNull JsonObject content) {
        long id = content.get(Keys.Turtle.ID).getAsLong();
        T turtle = this.getTurtleById(id, type);

        if (turtle == null) {
            // create new object
            turtle = this.getResourceBuilder().buildObject(type, content);
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
        this.removeCache(turtle.getId());
        UpdateHelper.ofDeleteTurtle(turtle);
    }

    private void updateCache(@NotNull Turtle turtle) throws IllegalArgumentException {
        // don't cache ephemeral JsonResources
        if ((turtle instanceof JsonResource j) && j.isEphemeral()) return;

        this.cache.add(turtle);
        this.logger.log(Level.FINER, "Updated cache for turtle " + turtle.getId() + ".");
    }

    public void removeCache(long id) {
        this.cache.removeById(id);
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
    public @Range(from = 0, to = Long.MAX_VALUE) long getTimeoutInbound() {
        return defaultTimeoutInbound;
    }

    @Override
    public void setTimeoutInbound(@Range(from = 0, to = Long.MAX_VALUE) long ms) {
        this.defaultTimeoutInbound = ms;
    }

    @Override
    public @Range(from = 0, to = Long.MAX_VALUE) long getTimeoutOutbound() {
        return defaultTimeoutOutbound;
    }

    @Override
    public void setTimeoutOutbound(@Range(from = 0, to = Long.MAX_VALUE) long ms) {
        this.defaultTimeoutOutbound = ms;
    }

    /* - - - */

    @Override
    public void shutdown() throws IOException {
        logger.log(Level.INFO, "Shutting down...");
        this.networkAdapter.shutdown();
        this.provider.shutdown();
        logger.log(Level.INFO, "OK bye.");
    }

    @Override
    public void shutdownNow() throws IOException {
        logger.log(Level.WARNING, "Forcing shutdown...");
        this.networkAdapter.shutdown();
        this.provider.shutdownNow();
        logger.log(Level.INFO, "OK bye.");
    }
}
