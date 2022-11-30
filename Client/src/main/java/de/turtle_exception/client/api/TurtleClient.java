package de.turtle_exception.client.api;

import de.turtle_exception.client.api.entities.*;
import de.turtle_exception.client.api.entities.containers.*;
import de.turtle_exception.client.api.entities.containers.messages.IChannelContainer;
import de.turtle_exception.client.api.entities.containers.messages.IDiscordChannelContainer;
import de.turtle_exception.client.api.entities.containers.messages.IMessageContainer;
import de.turtle_exception.client.api.entities.containers.messages.IMinecraftChannelContainer;
import de.turtle_exception.client.api.entities.messages.DiscordChannel;
import de.turtle_exception.client.api.entities.messages.MinecraftChannel;
import de.turtle_exception.client.api.entities.messages.SyncChannel;
import de.turtle_exception.client.api.entities.messages.SyncMessage;
import de.turtle_exception.client.api.event.EventListener;
import de.turtle_exception.client.api.event.EventManager;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.api.request.entities.*;
import de.turtle_exception.client.api.request.entities.messages.DiscordChannelAction;
import de.turtle_exception.client.api.request.entities.messages.MinecraftChannelAction;
import de.turtle_exception.client.api.request.entities.messages.SyncChannelAction;
import de.turtle_exception.client.api.request.entities.messages.SyncMessageAction;
import de.turtle_exception.client.internal.NetworkAdapter;
import de.turtle_exception.client.internal.Provider;
import de.turtle_exception.client.internal.net.NetClient;
import de.turtle_exception.client.internal.net.NetworkProvider;
import de.turtle_exception.client.internal.util.logging.NestedLogger;
import de.turtle_exception.client.internal.util.logging.SimpleFormatter;
import de.turtle_exception.client.internal.util.version.Version;
import net.dv8tion.jda.api.JDA;
import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

/**
 * The TurtleClient is the core of the API. It functions as the base interface to all features and is responsible for
 * caching and delegating tasks.
 * <p> A TurtleClient can be created by using the {@link TurtleClientBuilder}.
 * @see EventManager
 * @see Turtle
 */
@SuppressWarnings("unused")
public interface TurtleClient extends
        IUserContainer, IGroupContainer, IJsonResourceContainer, IProjectContainer, ITicketContainer,
        IDiscordChannelContainer, IMinecraftChannelContainer, IChannelContainer, IMessageContainer
{
    /**
     * Returns the root logger of the API.
     * <p> Every {@link Logger} that is used by parts of the API is a {@link NestedLogger} that will stream its output
     * to this Logger.
     * @return API logger.
     * @see NestedLogger
     * @see SimpleFormatter
     */
    @NotNull Logger getLogger();

    /**
     * Returns the version of this TurtleClient. This is mainly used internally to ensure safe communication with the
     * server but can also be used outside the API.
     * <p> This version can be represented as a String by simply calling {@link Version#toString()}.
     * @return Application version.
     */
    @NotNull Version getVersion();

    /**
     * Returns the EventManager of this TurtleClient. The EventManager can be used to register {@link EventListener
     * EventListeners}:
     * <pre> {@code
     * TurtleClient client = ...;
     * EventListener listener = new EventListener() {
     *             @Override
     *             public void onGenericEvent(@NotNull Event event) {
     *                 // ...
     *             }
     *         };
     * client.getEventManager().register(listener);
     * }</pre>
     * @return EventManager instance.
     */
    @NotNull EventManager getEventManager();

    /**
     * Returns the NetworkAdapter of this TurtleClient. This usually is a {@link NetClient}, unless a different
     * implementation has been set by {@link TurtleClientBuilder#setNetworkAdapter(NetworkAdapter)}.
     * @return NetworkAdapter instance.
     */
    @NotNull NetworkAdapter getNetworkAdapter();

    /**
     * Returns the Provider of this TurtleClient. This usually is a {@link NetworkProvider}, unless a different
     * implementation has been set by {@link TurtleClientBuilder#setProvider(Provider)}.
     * @return Provider instance.
     */
    @NotNull Provider getProvider();

    /**
     * Invalidates all caches this TurtleClient manages and (if instructed via the first parameter) retrieves all
     * available resources from the database to re-fill them.
     * <p> Note that doing so may cause temporary performance issues as this is an expensive call that will fire many
     * events and blocks the calling thread until all underlying Actions (i.e. retrieving Resources) are completed.
     * @param retrieve Whether the server should be queried for resources after clearing the caches.
     * @return Action that provides {@code void} on completion.
     */
    @SuppressWarnings("UnusedReturnValue")
    @NotNull Action<Void> invalidateCaches(boolean retrieve);

    /* - - - */

    @Override
    @NotNull List<Turtle> getTurtles();

    @Override
    @Nullable Turtle getTurtleById(long id);

    /* - - - */

    /**
     * Creates an Action with the Provider request to retrieve a {@link Group} specified by its id.
     * <p> If the operation is successful, the Group will also be put into cache, if not already present.
     * @return Action that provides the {@link Group} on completion.
     * @see TurtleClient#getGroupById(long)
     * @see Group#update()
     */
    @NotNull Action<Group> retrieveGroup(long id);

    /**
     * Creates an Action with the Provider request to retrieve all available {@link Group Groups}.
     * <p> If the operation is successful, the retrieved Groups will also be put into cache, if not already present.
     * @return Action that provides the List of {@link Group Groups} on completion.
     * @see TurtleClient#getGroups()
     */
    @NotNull Action<List<Group>> retrieveGroups();

    /**
     * Creates an Action with the Provider request to retrieve a {@link JsonResource} specified by its id.
     * <p> If the operation is successful and {@code ephemeral} is set to {@code true}, the JsonResource will also be
     * put into cache, if not already present.
     * @return Action that provides the {@link JsonResource} on completion.
     * @see TurtleClient#getJsonResourceById(long)
     * @see JsonResource#update()
     */
    // no retrieve-all because JsonResources aren't designed for that
    @NotNull Action<JsonResource> retrieveJsonResource(long id);

    /**
     * Creates an Action with the Provider request to retrieve a {@link Project} specified by its id.
     * <p> If the operation is successful, the Project will also be put into cache, if not already present.
     * @return Action that provides the {@link Project} on completion.
     * @see TurtleClient#getDiscordChannelById(long)
     * @see DiscordChannel#update()
     */
    @NotNull Action<Project> retrieveProject(long id);

    /**
     * Creates an Action with the Provider request to retrieve all available {@link Project Projects}.
     * <p> If the operation is successful, the retrieved Projects will also be put into cache, if not already present.
     * @return Action that provides the List of {@link Project Projects} on completion.
     * @see TurtleClient#getProjects()
     */
    @NotNull Action<List<Project>> retrieveProjects();

    /**
     * Creates an Action with the Provider request to retrieve a {@link Ticket} specified by its id.
     * <p> If the operation is successful, the Ticket will also be put into cache, if not already present.
     * @return Action that provides the {@link Ticket} on completion.
     * @see TurtleClient#getTicketById(long)
     * @see Ticket#update()
     */
    @NotNull Action<Ticket> retrieveTicket(long id);

    /**
     * Creates an Action with the Provider request to retrieve all available {@link Ticket Tickets}.
     * <p> If the operation is successful, the retrieved Tickets will also be put into cache, if not already present.
     * @return Action that provides the List of {@link Ticket Tickets} on completion.
     * @see TurtleClient#getTickets()
     */
    @NotNull Action<List<Ticket>> retrieveTickets();

    /**
     * Creates an Action with the Provider request to retrieve a {@link User} specified by its id.
     * <p> If the operation is successful, the User will also be put into cache, if not already present.
     * @return Action that provides the {@link User} on completion.
     * @see TurtleClient#getUserById(long)
     * @see User#update()
     */
    @NotNull Action<User> retrieveUser(long id);

    /**
     * Creates an Action with the Provider request to retrieve all available {@link User Users}.
     * <p> If the operation is successful, the retrieved Users will also be put into cache, if not already present.
     * @return Action that provides the List of {@link User Users} on completion.
     * @see TurtleClient#getUsers()
     */
    @NotNull Action<List<User>> retrieveUsers();

    // MESSAGES

    /**
     * Creates an Action with the Provider request to retrieve a {@link DiscordChannel} specified by its id.
     * <p> If the operation is successful, the DiscordChannel will also be put into cache, if not already present.
     * @return Action that provides the {@link DiscordChannel} on completion.
     * @see TurtleClient#getDiscordChannelById(long)
     * @see DiscordChannel#update()
     */
    @NotNull Action<DiscordChannel> retrieveDiscordChannel(long id);

    /**
     * Creates an Action with the Provider request to retrieve all available {@link DiscordChannel DiscordChannels}.
     * <p> If the operation is successful, the retrieved DiscordChannels will also be put into cache, if not already present.
     * @return Action that provides the List of {@link DiscordChannel DiscordChannels} on completion.
     * @see TurtleClient#getDiscordChannels()
     */
    @NotNull Action<List<DiscordChannel>> retrieveDiscordChannels();

    /**
     * Creates an Action with the Provider request to retrieve a {@link MinecraftChannel} specified by its id.
     * <p> If the operation is successful, the MinecraftChannel will also be put into cache, if not already present.
     * @return Action that provides the {@link MinecraftChannel} on completion.
     * @see TurtleClient#getMinecraftChannelById(long)
     * @see MinecraftChannel#update()
     */
    @NotNull Action<MinecraftChannel> retrieveMinecraftChannel(long id);

    /**
     * Creates an Action with the Provider request to retrieve all available {@link MinecraftChannel MinecraftChannels}.
     * <p> If the operation is successful, the retrieved MinecraftChannels will also be put into cache, if not already present.
     * @return Action that provides the List of {@link MinecraftChannel MinecraftChannels} on completion.
     * @see TurtleClient#getMinecraftChannels()
     */
    @NotNull Action<List<MinecraftChannel>> retrieveMinecraftChannels();

    /**
     * Creates an Action with the Provider request to retrieve a {@link SyncChannel} specified by its id.
     * <p> If the operation is successful, the Channel will also be put into cache, if not already present.
     * @return Action that provides the {@link SyncChannel} on completion.
     * @see TurtleClient#getChannelById(long)
     * @see SyncChannel#update()
     */
    @NotNull Action<SyncChannel> retrieveChannel(long id);

    /**
     * Creates an Action with the Provider request to retrieve all available {@link SyncChannel Channels}.
     * <p> If the operation is successful, the retrieved Channels will also be put into cache, if not already present.
     * @return Action that provides the List of {@link SyncChannel Channels} on completion.
     * @see TurtleClient#getChannels()
     */
    @NotNull Action<List<SyncChannel>> retrieveChannels();

    /**
     * Creates an Action with the Provider request to retrieve a {@link SyncMessage} specified by its id.
     * <p> If the operation is successful, the Message will also be put into cache, if not already present.
     * @return Action that provides the {@link SyncMessage} on completion.
     * @see TurtleClient#getMessageById(long)
     * @see SyncMessage#update()
     */
    // no retrieve-all because why
    @NotNull Action<SyncMessage> retrieveMessage(long id);

    /* - - - */

    /**
     * Creates an Action with the Provider request to create a new {@link Group}. The returned {@link GroupAction} may
     * be used to modify the request and to set each required field. If any required field is missing the server will
     * reject the request and respond with an error.
     * <p> If the operation is successful, the created Group will also be put into cache, if not already present.
     * @return Action that provides the newly crated {@link Group} on completion.
     */
    @NotNull GroupAction createGroup();

    /**
     * Creates an Action with the Provider request to create a new {@link JsonResource}. The returned
     * {@link JsonResourceAction} may be used to modify the request and to set each required field. If any required
     * field is missing the server will reject the request and respond with an error.
     * <p> If the operation is successful and {@code ephemeral} is set to {@code true}, the created JsonResource will
     * also be put into cache, if not already present.
     * @return Action that provides the newly crated {@link JsonResource} on completion.
     */
    @NotNull JsonResourceAction createJsonResource();

    /**
     * Creates an Action with the Provider request to create a new {@link Project}. The returned {@link ProjectAction}
     * may be used to modify the request and to set each required field. If any required field is missing the server
     * will reject the request and respond with an error.
     * <p> If the operation is successful, the created Project will also be put into cache, if not already present.
     * @return Action that provides the newly crated {@link Project} on completion.
     */
    @NotNull ProjectAction createProject();

    /**
     * Creates an Action with the Provider request to create a new {@link Ticket}. The returned {@link TicketAction} may
     * be used to modify the request and to set each required field. If any required field is missing the server will
     * reject the request and respond with an error.
     * <p> If the operation is successful, the created Ticket will also be put into cache, if not already present.
     * @return Action that provides the newly crated {@link Ticket} on completion.
     */
    @NotNull TicketAction createTicket();

    /**
     * Creates an Action with the Provider request to create a new {@link User}. The returned {@link UserAction} may
     * be used to modify the request and to set each required field. If any required field is missing the server will
     * reject the request and respond with an error.
     * <p> If the operation is successful, the created User will also be put into cache, if not already present.
     * @return Action that provides the newly crated {@link User} on completion.
     */
    @NotNull UserAction createUser();

    // MESSAGES

    /**
     * Creates an Action with the Provider request to create a new {@link DiscordChannel}. The returned
     * {@link DiscordChannelAction} may be used to modify the request and to set each required field. If any required
     * field is missing the server will reject the request and respond with an error.
     * <p> If the operation is successful, the created DiscordChannel will also be put into cache, if not already present.
     * @return Action that provides the newly crated {@link DiscordChannel} on completion.
     */
    @NotNull DiscordChannelAction createDiscordChannel();

    /**
     * Creates an Action with the Provider request to create a new {@link MinecraftChannel}. The returned
     * {@link MinecraftChannelAction} may be used to modify the request and to set each required field. If any required
     * field is missing the server will reject the request and respond with an error.
     * <p> If the operation is successful, the created MinecraftChannel will also be put into cache, if not already present.
     * @return Action that provides the newly crated {@link MinecraftChannel} on completion.
     */
    @NotNull MinecraftChannelAction createMinecraftChannel();

    /**
     * Creates an Action with the Provider request to create a new {@link SyncChannel}. The returned
     * {@link SyncChannelAction} may be used to modify the request and to set each required field. If any required field
     * is missing the server will reject the request and respond with an error.
     * <p> If the operation is successful, the created Channel will also be put into cache, if not already present.
     * @return Action that provides the newly crated {@link SyncChannel} on completion.
     */
    @NotNull SyncChannelAction createChannel();

    /**
     * Creates an Action with the Provider request to create a new {@link SyncMessage}. The returned
     * {@link SyncMessage} may be used to modify the request and to set each required field. If any required field is
     * missing the server will reject the request and respond with an error.
     * <p> If the operation is successful, the created Message will also be put into cache, if not already present.
     * @return Action that provides the newly crated {@link SyncMessage} on completion.
     */
    @NotNull SyncMessageAction createMessage();

    /* - - - */

    /**
     * Returns the dedicated {@link Server} instance of this TurtleClient, or {@code null} if none has been registered
     * via {@link TurtleClient#setSpigotServer(Server)}.
     * <p> The underlying attribute will never be modified internally and can only be set manually.
     * @return Dedicated {@link Server} (possibly {@code null}).
     */
    @Nullable Server getSpigotServer();

    /**
     * Registers a {@link Server} instance to be the dedicated Server of this TurtleClient. This makes usage of
     * convenience methods like {@link User#getMinecraft()} possible.
     * <p> The underlying attribute will never be modified internally and can only be set manually. Meaning exception
     * handling for mentioned convenience methods can safely be ignored if the provided argument is {@code != null}.
     * @param server {@link Server} instance, or {@code null} to reset.
     */
    void setSpigotServer(@Nullable Server server);

    /**
     * Returns the dedicated {@link JDA} instance of this TurtleClient, or {@code null} if none has been registered
     * via {@link TurtleClient#setJDA(JDA)}.
     * <p> The underlying attribute will never be modified internally and can only be set manually.
     * @return Dedicated {@link JDA} (possibly {@code null}).
     */
    @Nullable JDA getJDA();

    /**
     * Registers a {@link JDA} instance to be the dedicated JDA of this TurtleClient. This makes usage of convenience
     * methods like {@link User#getDiscord()}} possible.
     * <p> The underlying attribute will never be modified internally and can only be set manually. Meaning exception
     * handling for mentioned convenience methods can safely be ignored if the provided argument is {@code != null}.
     * @param jda {@link JDA} instance, or {@code null} to reset.
     */
    void setJDA(@Nullable JDA jda);

    /* - - - */

    /**
     * Returns the time in milliseconds an incoming packet may be processed before timing out.
     * @return Inbound timeout in ms.
     */
    @Range(from = 0, to = Long.MAX_VALUE) long getTimeoutInbound();

    /**
     * Sets the time in milliseconds an incoming packet may be processed before timing out.
     * <p> You should probably not use this method... ( ͠° ͟ʖ ͡°)
     * @param ms Inbound timeout in ms.
     */
    void setTimeoutInbound(@Range(from = 0, to = Long.MAX_VALUE) long ms);

    /**
     * Returns the time in milliseconds an outbound request may wait for a response before timing out.
     * @return Outbound timeout in ms.
     */
    @Range(from = 0, to = Long.MAX_VALUE) long getTimeoutOutbound();

    /**
     * Sets the time in milliseconds an outbound request may wait for a response before timing out.
     * <p> You should probably not use this method... ( ͠° ͟ʖ ͡°)
     * @param ms Outbound timeout in ms.
     */
    void setTimeoutOutbound(@Range(from = 0, to = Long.MAX_VALUE) long ms);

    /* - - - */

    /**
     * Returns the last ping to the server in milliseconds. This value is updated with every heartbeat sent by the
     * server and represents the round-trip time of a minimal packet.
     * @return Ping in ms.
     * @throws UnsupportedOperationException if the {@link NetworkAdapter} of this TurtleClient is not a {@link NetClient}.
     */
    default long getPing() throws UnsupportedOperationException {
        if (!(getNetworkAdapter() instanceof NetClient netClient))
            throw new UnsupportedOperationException("Only supported with NetClients");
        return netClient.getConnection().getPing();
    }

    /**
     * Awaits the execution of any remaining {@link Action Actions} in the {@link Provider} (within a timeout window),
     * notifies the {@link NetworkAdapter} to safely close the connection and shuts down this TurtleClient.
     * <p> A TurtleClient may not be re-used after shutting it down. Doing so would result in unpredictable behaviour.
     * Resource caches are not emptied on shutdown, and while this is not recommended, they can technically still be
     * accessed afterwards.
     * <p> This method will block the calling thread until the TurtleClient is shut down or the timeout is exceeded.
     * @throws IOException if such an exception is thrown while shutting down an underlying service.
     */
    void shutdown() throws IOException;

    /**
     * Forces this TurtleClient to shut down. Remaining {@link Action Actions} will not be executed and a safe
     * disconnect by the {@link NetworkAdapter} cannot be guaranteed.
     * @throws IOException if such an exception is thrown while shutting down an underlying service.
     */
    void shutdownNow() throws IOException;
}
