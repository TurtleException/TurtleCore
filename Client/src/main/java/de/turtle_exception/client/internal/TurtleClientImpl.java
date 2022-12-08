package de.turtle_exception.client.internal;

import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.*;
import de.turtle_exception.client.api.entities.attributes.EphemeralType;
import de.turtle_exception.client.api.entities.form.*;
import de.turtle_exception.client.api.entities.messages.DiscordChannel;
import de.turtle_exception.client.api.entities.messages.MinecraftChannel;
import de.turtle_exception.client.api.entities.messages.SyncChannel;
import de.turtle_exception.client.api.event.EventManager;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.api.request.entities.*;
import de.turtle_exception.client.api.request.entities.form.*;
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
import de.turtle_exception.client.internal.request.actions.entities.form.*;
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

    /** Resource types that should be pulled from the server by default. */
    private final List<Class<? extends Turtle>> retrievableTypes = List.of(
            Group.class, Project.class, Ticket.class, User.class,
            // FORM
            CompletedForm.class, QueryElement.class, QueryResponse.class, TemplateForm.class, TextElement.class,
            // MESSAGES
            DiscordChannel.class, MinecraftChannel.class, SyncChannel.class
    );

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
            for (Class<? extends Turtle> type : this.retrievableTypes) {
                this.retrieveTurtles(type).complete();
            }
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

    public <T extends Turtle> @NotNull Action<List<T>> retrieveTurtles(@NotNull Class<T> type) {
        return provider.get(type).andThenParseList(type).onSuccess(l -> {
            cache.removeAll(type);
            for (T t : l) {
                // don't cache ephemeral resources
                if (t instanceof EphemeralType e && e.isEphemeral()) continue;

                cache.add(t);
            }
        });
    }

    public <T extends Turtle> @NotNull Action<T> retrieveTurtle(long id, @NotNull Class<T> type) {
        return provider.get(type, id).andThenParse(type).onSuccess(t -> {
            cache.removeById(id);

            // don't cache ephemeral resources
            if (t instanceof EphemeralType e && e.isEphemeral()) return;

            cache.add(t);
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

    // FORM

    @Override
    public @NotNull CompletedFormAction createCompletedForm() {
        return new CompletedFormActionImpl(this.provider);
    }

    @Override
    public @NotNull QueryElementAction createQueryElement() {
        return new QueryElementActionImpl(this.provider);
    }

    @Override
    public @NotNull QueryResponseAction createQueryResponse() {
        return new QueryResponseActionImpl(this.provider);
    }

    @Override
    public @NotNull TemplateFormAction createTemplateForm() {
        return new TemplateFormActionImpl(this.provider);
    }

    @Override
    public @NotNull TextElementAction createTextElement() {
        return new TextElementActionImpl(this.provider);
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
