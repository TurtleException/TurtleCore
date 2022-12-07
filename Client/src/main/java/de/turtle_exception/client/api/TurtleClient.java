package de.turtle_exception.client.api;

import de.turtle_exception.client.api.entities.*;
import de.turtle_exception.client.api.entities.containers.TurtleContainer;
import de.turtle_exception.client.api.entities.form.*;
import de.turtle_exception.client.api.entities.messages.*;
import de.turtle_exception.client.api.event.EventListener;
import de.turtle_exception.client.api.event.EventManager;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.api.request.entities.*;
import de.turtle_exception.client.api.request.entities.form.*;
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
public interface TurtleClient extends TurtleContainer<Turtle> {
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

    default @NotNull List<Group> getGroups() {
        return this.getTurtles(Group.class);
    }

    default @Nullable Group getGroupById(long id) {
        return this.getTurtleById(id, Group.class);
    }

    default @NotNull List<JsonResource> getJsonResources() {
        return this.getTurtles(JsonResource.class);
    }

    default @Nullable JsonResource getJsonResourceById(long id) {
        return this.getTurtleById(id, JsonResource.class);
    }

    default @NotNull List<Project> getProjects() {
        return this.getTurtles(Project.class);
    }

    default @Nullable Project getProjectById(long id) {
        return this.getTurtleById(id, Project.class);
    }

    default @NotNull List<Ticket> getTickets() {
        return this.getTurtles(Ticket.class);
    }

    default @Nullable Ticket getTicketById(long id) {
        return this.getTurtleById(id, Ticket.class);
    }

    default @NotNull List<User> getUsers() {
        return this.getTurtles(User.class);
    }

    default @Nullable User getUserById(long id) {
        return this.getTurtleById(id, User.class);
    }

    // FORM

    default @NotNull List<CompletedForm> getCompletedForms() {
        return this.getTurtles(CompletedForm.class);
    }

    default @Nullable CompletedForm getCompletedFormById(long id) {
        return this.getTurtleById(id, CompletedForm.class);
    }

    default @NotNull List<Element> getElements() {
        return this.getTurtles(Element.class);
    }

    default @Nullable Element getElementFormById(long id) {
        return this.getTurtleById(id, Element.class);
    }

    default @NotNull List<QueryElement> getQueryElements() {
        return this.getTurtles(QueryElement.class);
    }

    default @Nullable QueryElement getQueryElementById(long id) {
        return this.getTurtleById(id, QueryElement.class);
    }

    default @NotNull List<QueryResponse> getQueryResponses() {
        return this.getTurtles(QueryResponse.class);
    }

    default @Nullable QueryResponse getQueryResponseById(long id) {
        return this.getTurtleById(id, QueryResponse.class);
    }

    default @NotNull List<TemplateForm> getTemplateForms() {
        return this.getTurtles(TemplateForm.class);
    }

    default @Nullable TemplateForm getTemplateFormById(long id) {
        return this.getTurtleById(id, TemplateForm.class);
    }

    default @NotNull List<TextElement> getTextElements() {
        return this.getTurtles(TextElement.class);
    }

    default @Nullable TextElement getTextElementById(long id) {
        return this.getTurtleById(id, TextElement.class);
    }

    // MESSAGES

    default @NotNull List<DiscordChannel> getDiscordChannels() {
        return this.getTurtles(DiscordChannel.class);
    }

    default @Nullable DiscordChannel getDiscordChannelById(long id) {
        return this.getTurtleById(id, DiscordChannel.class);
    }

    default @NotNull List<IChannel> getIChannels() {
        return this.getTurtles(IChannel.class);
    }

    default @Nullable IChannel getIChannelById(long id) {
        return this.getTurtleById(id, IChannel.class);
    }

    default @NotNull List<MinecraftChannel> getMinecraftChannels() {
        return this.getTurtles(MinecraftChannel.class);
    }

    default @Nullable MinecraftChannel getMinecraftChannelById(long id) {
        return this.getTurtleById(id, MinecraftChannel.class);
    }

    default @NotNull List<SyncChannel> getSyncChannels() {
        return this.getTurtles(SyncChannel.class);
    }

    default @Nullable SyncChannel getSyncChannelById(long id) {
        return this.getTurtleById(id, SyncChannel.class);
    }

    default @NotNull List<SyncMessage> getSyncMessages() {
        return this.getTurtles(SyncMessage.class);
    }

    default @Nullable SyncMessage getSyncMessageById(long id) {
        return this.getTurtleById(id, SyncMessage.class);
    }

    /* - - - */

    /**
     * Creates an Action with the Provider request to retrieve all available {@link Turtle Turtles} of type {@code T}.
     * <p> If the operation is successful, the retrieved objects will also be put into cache, if not already present.
     * @return Action that provides the List of Turtles on completion.
     */
    <T extends Turtle> @NotNull Action<List<T>> retrieveTurtles(@NotNull Class<T> type);

    /**
     * Creates an Action with the Provider request to retrieve a {@link Turtle} of type {@code T}, specified by its id.
     * <p> If the operation is successful, the object will also be put into cache, if not already present.
     * @return Action that provides the Turtle on completion.
     * @see Turtle#update()
     */
    <T extends Turtle> @NotNull Action<T> retrieveTurtle(long id, @NotNull Class<T> type);

    default @NotNull Action<List<Group>> retrieveGroups() {
        return this.retrieveTurtles(Group.class);
    }

    default @NotNull Action<Group> retrieveGroup(long id) {
        return this.retrieveTurtle(id, Group.class);
    }

    // no retrieveJsonResources() to discourage users from retrieving ALL JsonResources at once

    default @NotNull Action<JsonResource> retrieveJsonResource(long id) {
        return this.retrieveTurtle(id, JsonResource.class);
    }

    default @NotNull Action<List<Project>> retrieveProjects() {
        return this.retrieveTurtles(Project.class);
    }

    default @NotNull Action<Project> retrieveProject(long id) {
        return this.retrieveTurtle(id, Project.class);
    }

    default @NotNull Action<List<Ticket>> retrieveTickets() {
        return this.retrieveTurtles(Ticket.class);
    }

    default @NotNull Action<Ticket> retrieveTicket(long id) {
        return this.retrieveTurtle(id, Ticket.class);
    }

    default @NotNull Action<List<User>> retrieveUsers() {
        return this.retrieveTurtles(User.class);
    }

    default @NotNull Action<User> retrieveUser(long id) {
        return this.retrieveTurtle(id, User.class);
    }

    // FORM

    default @NotNull Action<List<CompletedForm>> retrieveCompletedForms() {
        return this.retrieveTurtles(CompletedForm.class);
    }

    default @NotNull Action<CompletedForm> retrieveCompletedForm(long id) {
        return this.retrieveTurtle(id, CompletedForm.class);
    }

    default @NotNull Action<List<QueryElement>> retrieveQueryElements() {
        return this.retrieveTurtles(QueryElement.class);
    }

    default @NotNull Action<QueryElement> retrieveQueryElement(long id) {
        return this.retrieveTurtle(id, QueryElement.class);
    }

    default @NotNull Action<List<QueryResponse>> retrieveQueryResponses() {
        return this.retrieveTurtles(QueryResponse.class);
    }

    default @NotNull Action<QueryResponse> retrieveQueryResponse(long id) {
        return this.retrieveTurtle(id, QueryResponse.class);
    }

    default @NotNull Action<List<TemplateForm>> retrieveTemplateForms() {
        return this.retrieveTurtles(TemplateForm.class);
    }

    default @NotNull Action<TemplateForm> retrieveTemplateForm(long id) {
        return this.retrieveTurtle(id, TemplateForm.class);
    }

    default @NotNull Action<List<TextElement>> retrieveTextElements() {
        return this.retrieveTurtles(TextElement.class);
    }

    default @NotNull Action<TextElement> retrieveTextElement(long id) {
        return this.retrieveTurtle(id, TextElement.class);
    }

    // MESSAGE

    default @NotNull Action<List<DiscordChannel>> retrieveDiscordChannels() {
        return this.retrieveTurtles(DiscordChannel.class);
    }

    default @NotNull Action<DiscordChannel> retrieveDiscordChannel(long id) {
        return this.retrieveTurtle(id, DiscordChannel.class);
    }

    default @NotNull Action<List<MinecraftChannel>> retrieveMinecraftChannels() {
        return this.retrieveTurtles(MinecraftChannel.class);
    }

    default @NotNull Action<MinecraftChannel> retrieveMinecraftChannel(long id) {
        return this.retrieveTurtle(id, MinecraftChannel.class);
    }

    default @NotNull Action<List<SyncChannel>> retrieveSyncChannels() {
        return this.retrieveTurtles(SyncChannel.class);
    }

    default @NotNull Action<SyncChannel> retrieveSyncChannel(long id) {
        return this.retrieveTurtle(id, SyncChannel.class);
    }

    // no retrieveSyncMessages() to discourage users from retrieving ALL SyncMessages at once

    default @NotNull Action<SyncMessage> retrieveSyncMessage(long id) {
        return this.retrieveTurtle(id, SyncMessage.class);
    }

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

    // FORM

    @NotNull CompletedFormAction createCompletedForm();

    @NotNull QueryElementAction createQueryElement();

    @NotNull QueryResponseAction createQueryResponse();

    @NotNull TemplateFormAction createTemplateForm();

    @NotNull TextElementAction createTextElement();

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
