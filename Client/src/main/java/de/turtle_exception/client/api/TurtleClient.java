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
import de.turtle_exception.client.api.request.entities.messages.*;
import de.turtle_exception.client.internal.NetworkAdapter;
import de.turtle_exception.client.internal.Provider;
import de.turtle_exception.client.internal.net.NetClient;
import de.turtle_exception.client.internal.net.NetworkProvider;
import de.turtle_exception.client.internal.util.logging.NestedLogger;
import de.turtle_exception.client.internal.util.logging.SimpleFormatter;
import de.turtle_exception.client.internal.util.version.Version;
import de.turtle_exception.fancyformat.FancyFormatter;
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
     * Return the {@link FancyFormatter} of this TurtleClient. This formatter is used globally. Any modifications to it
     * will impact formatting on every layer of the API.
     * @return FancyFormatter instance.
     */
    @NotNull FancyFormatter getFormatter();

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

    /**
     * Returns an immutable List of all cached {@link Group} objects.
     * @return List of cached Groups.
     */
    default @NotNull List<Group> getGroups() {
        return this.getTurtles(Group.class);
    }

    /**
     * Returns a single {@link Group} specified by its id, or {@code null} if no such object is stored in the underlying
     * cache.
     * @param id The unique id of the Group.
     * @return The requested Group (may be {@code null}).
     * @see Group#getId()
     */
    default @Nullable Group getGroupById(long id) {
        return this.getTurtleById(id, Group.class);
    }

    /**
     * Returns an immutable List of all cached {@link JsonResource} objects.
     * @return List of cached JsonResources.
     */
    default @NotNull List<JsonResource> getJsonResources() {
        return this.getTurtles(JsonResource.class);
    }

    /**
     * Returns a single {@link JsonResource} specified by its id, or {@code null} if no such object is stored in the
     * underlying cache.
     * @param id The unique id of the JsonResource.
     * @return The requested JsonResource (may be {@code null}).
     * @see JsonResource#getId()
     */
    default @Nullable JsonResource getJsonResourceById(long id) {
        return this.getTurtleById(id, JsonResource.class);
    }

    /**
     * Returns an immutable List of all cached {@link Project} objects.
     * @return List of cached Projects.
     */
    default @NotNull List<Project> getProjects() {
        return this.getTurtles(Project.class);
    }

    /**
     * Returns a single {@link Project} specified by its id, or {@code null} if no such object is stored in the
     * underlying cache.
     * @param id The unique id of the Project.
     * @return The requested Project (may be {@code null}).
     * @see Project#getId()
     */
    default @Nullable Project getProjectById(long id) {
        return this.getTurtleById(id, Project.class);
    }

    /**
     * Returns an immutable List of all cached {@link Ticket} objects.
     * @return List of cached Tickets.
     */
    default @NotNull List<Ticket> getTickets() {
        return this.getTurtles(Ticket.class);
    }

    /**
     * Returns a single {@link Ticket} specified by its id, or {@code null} if no such object is stored in the
     * underlying cache.
     * @param id The unique id of the Ticket.
     * @return The requested Ticket (may be {@code null}).
     * @see Ticket#getId()
     */
    default @Nullable Ticket getTicketById(long id) {
        return this.getTurtleById(id, Ticket.class);
    }

    /**
     * Returns an immutable List of all cached {@link User} objects.
     * @return List of cached Users.
     */
    default @NotNull List<User> getUsers() {
        return this.getTurtles(User.class);
    }

    /**
     * Returns a single {@link User} specified by its id, or {@code null} if no such object is stored in the underlying
     * cache.
     * @param id The unique id of the User.
     * @return The requested User (may be {@code null}).
     * @see User#getId()
     */
    default @Nullable User getUserById(long id) {
        return this.getTurtleById(id, User.class);
    }

    // FORM

    /**
     * Returns an immutable List of all cached {@link CompletedForm} objects.
     * @return List of cached CompletedForms.
     */
    default @NotNull List<CompletedForm> getCompletedForms() {
        return this.getTurtles(CompletedForm.class);
    }

    /**
     * Returns a single {@link CompletedForm} specified by its id, or {@code null} if no such object is stored in the
     * underlying cache.
     * @param id The unique id of the Form.
     * @return The requested Form (may be {@code null}).
     * @see CompletedForm#getId()
     */
    default @Nullable CompletedForm getCompletedFormById(long id) {
        return this.getTurtleById(id, CompletedForm.class);
    }

    /**
     * Returns an immutable List of all cached {@link Element} objects.
     * @return List of cached Elements.
     */
    default @NotNull List<Element> getElements() {
        return this.getTurtles(Element.class);
    }

    /**
     * Returns a single {@link Element} specified by its id, or {@code null} if no such object is stored in the
     * underlying cache.
     * @param id The unique id of the Element.
     * @return The requested Element (may be {@code null}).
     * @see Element#getId()
     */
    default @Nullable Element getElementFormById(long id) {
        return this.getTurtleById(id, Element.class);
    }

    /**
     * Returns an immutable List of all cached {@link QueryElement} objects.
     * @return List of cached QueryElements.
     */
    default @NotNull List<QueryElement> getQueryElements() {
        return this.getTurtles(QueryElement.class);
    }

    /**
     * Returns a single {@link QueryElement} specified by its id, or {@code null} if no such object is stored in the
     * underlying cache.
     * @param id The unique id of the Element.
     * @return The requested Element (may be {@code null}).
     * @see QueryElement#getId()
     */
    default @Nullable QueryElement getQueryElementById(long id) {
        return this.getTurtleById(id, QueryElement.class);
    }

    /**
     * Returns an immutable List of all cached {@link QueryResponse} objects.
     * @return List of cached QueryResponses.
     */
    default @NotNull List<QueryResponse> getQueryResponses() {
        return this.getTurtles(QueryResponse.class);
    }

    /**
     * Returns a single {@link QueryResponse} specified by its id, or {@code null} if no such object is stored in the
     * underlying cache.
     * @param id The unique id of the QueryResponse.
     * @return The requested QueryResponse (may be {@code null}).
     * @see QueryResponse#getId()
     */
    default @Nullable QueryResponse getQueryResponseById(long id) {
        return this.getTurtleById(id, QueryResponse.class);
    }

    /**
     * Returns an immutable List of all cached {@link TemplateForm} objects.
     * @return List of cached TemplateForms.
     */
    default @NotNull List<TemplateForm> getTemplateForms() {
        return this.getTurtles(TemplateForm.class);
    }

    /**
     * Returns a single {@link TemplateForm} specified by its id, or {@code null} if no such object is stored in the
     * underlying cache.
     * @param id The unique id of the Form.
     * @return The requested Form (may be {@code null}).
     * @see TemplateForm#getId()
     */
    default @Nullable TemplateForm getTemplateFormById(long id) {
        return this.getTurtleById(id, TemplateForm.class);
    }

    /**
     * Returns an immutable List of all cached {@link TextElement} objects.
     * @return List of cached TextElements.
     */
    default @NotNull List<TextElement> getTextElements() {
        return this.getTurtles(TextElement.class);
    }

    /**
     * Returns a single {@link TextElement} specified by its id, or {@code null} if no such object is stored in the
     * underlying cache.
     * @param id The unique id of the Element.
     * @return The requested Element (may be {@code null}).
     * @see TextElement#getId()
     */
    default @Nullable TextElement getTextElementById(long id) {
        return this.getTurtleById(id, TextElement.class);
    }

    // MESSAGES

    /**
     * Returns an immutable List of all cached {@link Attachment} objects.
     * @return List of cached Attachments.
     */
    default @NotNull List<Attachment> getAttachments() {
        return this.getTurtles(Attachment.class);
    }

    /**
     * Returns a single {@link Attachment} specified by its id, or {@code null} if no such object is stored in the
     * underlying cache.
     * @param id The unique id of the Attachment.
     * @return The requested Attachment (may be {@code null}).
     * @see Attachment#getId()
     */
    default @Nullable Attachment getAttachmentById(long id) {
        return this.getTurtleById(id, Attachment.class);
    }

    /**
     * Returns an immutable List of all cached {@link DiscordChannel} objects.
     * @return List of cached DiscordChannels.
     */
    default @NotNull List<DiscordChannel> getDiscordChannels() {
        return this.getTurtles(DiscordChannel.class);
    }

    /**
     * Returns a single {@link DiscordChannel} specified by its id, or {@code null} if no such object is stored in the
     * underlying cache.
     * @param id The unique id of the Channel.
     * @return The requested Channel (may be {@code null}).
     * @see DiscordChannel#getId()
     */
    default @Nullable DiscordChannel getDiscordChannelById(long id) {
        return this.getTurtleById(id, DiscordChannel.class);
    }

    /**
     * Returns an immutable List of all cached {@link IChannel} objects.
     * @return List of cached IChannels.
     */
    default @NotNull List<IChannel> getIChannels() {
        return this.getTurtles(IChannel.class);
    }

    /**
     * Returns a single {@link IChannel} specified by its id, or {@code null} if no such object is stored in the
     * underlying cache.
     * @param id The unique id of the Channel.
     * @return The requested Channel (may be {@code null}).
     * @see IChannel#getId()
     */
    default @Nullable IChannel getIChannelById(long id) {
        return this.getTurtleById(id, IChannel.class);
    }

    /**
     * Returns an immutable List of all cached {@link MinecraftChannel} objects.
     * @return List of cached MinecraftChannels.
     */
    default @NotNull List<MinecraftChannel> getMinecraftChannels() {
        return this.getTurtles(MinecraftChannel.class);
    }

    /**
     * Returns a single {@link MinecraftChannel} specified by its id, or {@code null} if no such object is stored in the
     * underlying cache.
     * @param id The unique id of the Channel.
     * @return The requested Channel (may be {@code null}).
     * @see MinecraftChannel#getId()
     */
    default @Nullable MinecraftChannel getMinecraftChannelById(long id) {
        return this.getTurtleById(id, MinecraftChannel.class);
    }

    /**
     * Returns an immutable List of all cached {@link SyncChannel} objects.
     * @return List of cached SyncChannels.
     */
    default @NotNull List<SyncChannel> getSyncChannels() {
        return this.getTurtles(SyncChannel.class);
    }

    /**
     * Returns a single {@link SyncChannel} specified by its id, or {@code null} if no such object is stored in the
     * underlying cache.
     * @param id The unique id of the Channel.
     * @return The requested Channel (may be {@code null}).
     * @see SyncChannel#getId()
     */
    default @Nullable SyncChannel getSyncChannelById(long id) {
        return this.getTurtleById(id, SyncChannel.class);
    }

    /**
     * Returns an immutable List of all cached {@link SyncMessage} objects.
     * @return List of cached SyncMessages.
     */
    default @NotNull List<SyncMessage> getSyncMessages() {
        return this.getTurtles(SyncMessage.class);
    }

    /**
     * Returns a single {@link SyncMessage} specified by its id, or {@code null} if no such object is stored in the
     * underlying cache.
     * @param id The unique id of the Message.
     * @return The requested Message (may be {@code null}).
     * @see SyncMessage#getId()
     */
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

    /**
     * Creates an Action with the Provider request to retrieve all available {@link Group Groups}.
     * <p> If the operation is successful, the retrieved Groups will also be put into cache, if not already present.
     * @return Action that provides the List of {@link Group Groups} on completion.
     * @see TurtleClient#getGroups()
     */
    default @NotNull Action<List<Group>> retrieveGroups() {
        return this.retrieveTurtles(Group.class);
    }

    /**
     * Creates an Action with the Provider request to retrieve a {@link Group} specified by its id.
     * <p> If the operation is successful, the Group will also be put into cache, if not already present.
     * @return Action that provides the {@link Group} on completion.
     * @see TurtleClient#getGroupById(long)
     * @see Group#update()
     */
    default @NotNull Action<Group> retrieveGroup(long id) {
        return this.retrieveTurtle(id, Group.class);
    }

    // no retrieveJsonResources() to discourage users from retrieving ALL JsonResources at once

    /**
     * Creates an Action with the Provider request to retrieve a {@link JsonResource} specified by its id.
     * <p> If the operation is successful, the JsonResource will also be put into cache, if not already present.
     * @return Action that provides the {@link JsonResource} on completion.
     * @see TurtleClient#getJsonResourceById(long)
     * @see JsonResource#update()
     */
    default @NotNull Action<JsonResource> retrieveJsonResource(long id) {
        return this.retrieveTurtle(id, JsonResource.class);
    }

    /**
     * Creates an Action with the Provider request to retrieve all available {@link Project Projects}.
     * <p> If the operation is successful, the retrieved Projects will also be put into cache, if not already present.
     * @return Action that provides the List of {@link Project Projects} on completion.
     * @see TurtleClient#getProjects()
     */
    default @NotNull Action<List<Project>> retrieveProjects() {
        return this.retrieveTurtles(Project.class);
    }

    /**
     * Creates an Action with the Provider request to retrieve a {@link Project} specified by its id.
     * <p> If the operation is successful, the Project will also be put into cache, if not already present.
     * @return Action that provides the {@link Project} on completion.
     * @see TurtleClient#getProjectById(long)
     * @see Project#update()
     */
    default @NotNull Action<Project> retrieveProject(long id) {
        return this.retrieveTurtle(id, Project.class);
    }

    /**
     * Creates an Action with the Provider request to retrieve all available {@link Ticket Tickets}.
     * <p> If the operation is successful, the retrieved Tickets will also be put into cache, if not already present.
     * @return Action that provides the List of {@link Ticket Tickets} on completion.
     * @see TurtleClient#getTickets()
     */
    default @NotNull Action<List<Ticket>> retrieveTickets() {
        return this.retrieveTurtles(Ticket.class);
    }

    /**
     * Creates an Action with the Provider request to retrieve a {@link Ticket} specified by its id.
     * <p> If the operation is successful, the Ticket will also be put into cache, if not already present.
     * @return Action that provides the {@link Ticket} on completion.
     * @see TurtleClient#getTicketById(long)
     * @see Ticket#update()
     */
    default @NotNull Action<Ticket> retrieveTicket(long id) {
        return this.retrieveTurtle(id, Ticket.class);
    }

    /**
     * Creates an Action with the Provider request to retrieve all available {@link User Users}.
     * <p> If the operation is successful, the retrieved Users will also be put into cache, if not already present.
     * @return Action that provides the List of {@link User Users} on completion.
     * @see TurtleClient#getUsers()
     */
    default @NotNull Action<List<User>> retrieveUsers() {
        return this.retrieveTurtles(User.class);
    }

    /**
     * Creates an Action with the Provider request to retrieve a {@link User} specified by its id.
     * <p> If the operation is successful, the User will also be put into cache, if not already present.
     * @return Action that provides the {@link User} on completion.
     * @see TurtleClient#getUserById(long)
     * @see User#update()
     */
    default @NotNull Action<User> retrieveUser(long id) {
        return this.retrieveTurtle(id, User.class);
    }

    // FORM

    /**
     * Creates an Action with the Provider request to retrieve all available {@link CompletedForm CompletedForms}.
     * <p> If the operation is successful, the retrieved Forms will also be put into cache, if not already present.
     * @return Action that provides the List of {@link CompletedForm CompletedForms} on completion.
     * @see TurtleClient#getCompletedForms()
     */
    default @NotNull Action<List<CompletedForm>> retrieveCompletedForms() {
        return this.retrieveTurtles(CompletedForm.class);
    }

    /**
     * Creates an Action with the Provider request to retrieve a {@link CompletedForm} specified by its id.
     * <p> If the operation is successful, the Form will also be put into cache, if not already present.
     * @return Action that provides the {@link CompletedForm} on completion.
     * @see TurtleClient#getCompletedFormById(long)
     * @see CompletedForm#update()
     */
    default @NotNull Action<CompletedForm> retrieveCompletedForm(long id) {
        return this.retrieveTurtle(id, CompletedForm.class);
    }

    /**
     * Creates an Action with the Provider request to retrieve all available {@link QueryElement QueryElements}.
     * <p> If the operation is successful, the retrieved Elements will also be put into cache, if not already present.
     * @return Action that provides the List of {@link QueryElement QueryElements} on completion.
     * @see TurtleClient#getQueryElements()
     */
    default @NotNull Action<List<QueryElement>> retrieveQueryElements() {
        return this.retrieveTurtles(QueryElement.class);
    }

    /**
     * Creates an Action with the Provider request to retrieve a {@link QueryElement} specified by its id.
     * <p> If the operation is successful, the Element will also be put into cache, if not already present.
     * @return Action that provides the {@link QueryElement} on completion.
     * @see TurtleClient#getQueryElementById(long)
     * @see QueryElement#update()
     */
    default @NotNull Action<QueryElement> retrieveQueryElement(long id) {
        return this.retrieveTurtle(id, QueryElement.class);
    }

    /**
     * Creates an Action with the Provider request to retrieve all available {@link QueryResponse QueryResponses}.
     * <p> If the operation is successful, the retrieved QueryResponses will also be put into cache, if not already present.
     * @return Action that provides the List of {@link QueryResponse QueryResponses} on completion.
     * @see TurtleClient#getQueryResponses()
     */
    default @NotNull Action<List<QueryResponse>> retrieveQueryResponses() {
        return this.retrieveTurtles(QueryResponse.class);
    }

    /**
     * Creates an Action with the Provider request to retrieve a {@link QueryResponse} specified by its id.
     * <p> If the operation is successful, the QueryResponse will also be put into cache, if not already present.
     * @return Action that provides the {@link QueryResponse} on completion.
     * @see TurtleClient#getQueryResponseById(long)
     * @see QueryResponse#update()
     */
    default @NotNull Action<QueryResponse> retrieveQueryResponse(long id) {
        return this.retrieveTurtle(id, QueryResponse.class);
    }

    /**
     * Creates an Action with the Provider request to retrieve all available {@link TemplateForm TemplateForms}.
     * <p> If the operation is successful, the retrieved Forms will also be put into cache, if not already present.
     * @return Action that provides the List of {@link TemplateForm TemplateForms} on completion.
     * @see TurtleClient#getTemplateForms()
     */
    default @NotNull Action<List<TemplateForm>> retrieveTemplateForms() {
        return this.retrieveTurtles(TemplateForm.class);
    }

    /**
     * Creates an Action with the Provider request to retrieve a {@link TemplateForm} specified by its id.
     * <p> If the operation is successful, the Form will also be put into cache, if not already present.
     * @return Action that provides the {@link TemplateForm} on completion.
     * @see TurtleClient#getTemplateFormById(long)
     * @see TemplateForm#update()
     */
    default @NotNull Action<TemplateForm> retrieveTemplateForm(long id) {
        return this.retrieveTurtle(id, TemplateForm.class);
    }

    /**
     * Creates an Action with the Provider request to retrieve all available {@link TextElement TextElements}.
     * <p> If the operation is successful, the retrieved Elements will also be put into cache, if not already present.
     * @return Action that provides the List of {@link TextElement TextElements} on completion.
     * @see TurtleClient#getTextElements()
     */
    default @NotNull Action<List<TextElement>> retrieveTextElements() {
        return this.retrieveTurtles(TextElement.class);
    }

    /**
     * Creates an Action with the Provider request to retrieve a {@link TextElement} specified by its id.
     * <p> If the operation is successful, the Element will also be put into cache, if not already present.
     * @return Action that provides the {@link TextElement} on completion.
     * @see TurtleClient#getTextElementById(long)
     * @see TextElement#update()
     */
    default @NotNull Action<TextElement> retrieveTextElement(long id) {
        return this.retrieveTurtle(id, TextElement.class);
    }

    // MESSAGE

    /**
     * Creates an Action with the Provider request to retrieve all available {@link Attachment Attachments}.
     * <p> If the operation is successful, the retrieved Attachments will also be put into cache, if not already present.
     * @return Action that provides the List of {@link Attachment Attachments} on completion.
     * @see TurtleClient#getAttachments()
     */
    default @NotNull Action<List<Attachment>> retrieveAttachments() {
        return this.retrieveTurtles(Attachment.class);
    }

    /**
     * Creates an Action with the Provider request to retrieve a {@link Attachment} specified by its id.
     * <p> If the operation is successful, the Attachment will also be put into cache, if not already present.
     * @return Action that provides the {@link Attachment} on completion.
     * @see TurtleClient#getAttachmentById(long)
     * @see Attachment#update()
     */
    default @NotNull Action<Attachment> retrieveAttachment(long id) {
        return this.retrieveTurtle(id, Attachment.class);
    }

    /**
     * Creates an Action with the Provider request to retrieve all available {@link DiscordChannel DiscordChannels}.
     * <p> If the operation is successful, the retrieved Channels will also be put into cache, if not already present.
     * @return Action that provides the List of {@link DiscordChannel DiscordChannels} on completion.
     * @see TurtleClient#getDiscordChannels()
     */
    default @NotNull Action<List<DiscordChannel>> retrieveDiscordChannels() {
        return this.retrieveTurtles(DiscordChannel.class);
    }

    /**
     * Creates an Action with the Provider request to retrieve a {@link DiscordChannel} specified by its id.
     * <p> If the operation is successful, the Channel will also be put into cache, if not already present.
     * @return Action that provides the {@link DiscordChannel} on completion.
     * @see TurtleClient#getDiscordChannelById(long)
     * @see DiscordChannel#update()
     */
    default @NotNull Action<DiscordChannel> retrieveDiscordChannel(long id) {
        return this.retrieveTurtle(id, DiscordChannel.class);
    }

    /**
     * Creates an Action with the Provider request to retrieve all available {@link MinecraftChannel MinecraftChannels}.
     * <p> If the operation is successful, the retrieved Channels will also be put into cache, if not already present.
     * @return Action that provides the List of {@link MinecraftChannel MinecraftChannels} on completion.
     * @see TurtleClient#getMinecraftChannels()
     */
    default @NotNull Action<List<MinecraftChannel>> retrieveMinecraftChannels() {
        return this.retrieveTurtles(MinecraftChannel.class);
    }

    /**
     * Creates an Action with the Provider request to retrieve a {@link MinecraftChannel} specified by its id.
     * <p> If the operation is successful, the Channel will also be put into cache, if not already present.
     * @return Action that provides the {@link MinecraftChannel} on completion.
     * @see TurtleClient#getMinecraftChannelById(long)
     * @see MinecraftChannel#update()
     */
    default @NotNull Action<MinecraftChannel> retrieveMinecraftChannel(long id) {
        return this.retrieveTurtle(id, MinecraftChannel.class);
    }

    /**
     * Creates an Action with the Provider request to retrieve all available {@link SyncChannel SyncChannels}.
     * <p> If the operation is successful, the retrieved Channels will also be put into cache, if not already present.
     * @return Action that provides the List of {@link SyncChannel SyncChannels} on completion.
     * @see TurtleClient#getSyncChannels()
     */
    default @NotNull Action<List<SyncChannel>> retrieveSyncChannels() {
        return this.retrieveTurtles(SyncChannel.class);
    }

    /**
     * Creates an Action with the Provider request to retrieve a {@link SyncChannel} specified by its id.
     * <p> If the operation is successful, the Channel will also be put into cache, if not already present.
     * @return Action that provides the {@link SyncChannel} on completion.
     * @see TurtleClient#getSyncChannelById(long)
     * @see SyncChannel#update()
     */
    default @NotNull Action<SyncChannel> retrieveSyncChannel(long id) {
        return this.retrieveTurtle(id, SyncChannel.class);
    }

    // no retrieveSyncMessages() to discourage users from retrieving ALL SyncMessages at once

    /**
     * Creates an Action with the Provider request to retrieve a {@link SyncMessage} specified by its id.
     * <p> If the operation is successful, the Message will also be put into cache, if not already present.
     * @return Action that provides the {@link SyncMessage} on completion.
     * @see TurtleClient#getSyncMessageById(long)
     * @see SyncMessage#update()
     */
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

    /**
     * Creates an Action with the Provider request to create a new {@link CompletedForm}. The returned
     * {@link CompletedFormAction} may be used to modify the request and to set each required field. If any required
     * field is missing the server will reject the request and respond with an error.
     * <p> If the operation is successful, the created Form will also be put into cache, if not already present.
     * @return Action that provides the newly crated {@link CompletedForm} on completion.
     */
    @NotNull CompletedFormAction createCompletedForm();

    /**
     * Creates an Action with the Provider request to create a new {@link QueryElement}. The returned
     * {@link QueryElementAction} may be used to modify the request and to set each required field. If any required
     * field is missing the server will reject the request and respond with an error.
     * <p> If the operation is successful, the created Element will also be put into cache, if not already present.
     * @return Action that provides the newly crated {@link User} on completion.
     */
    @NotNull QueryElementAction createQueryElement();

    /**
     * Creates an Action with the Provider request to create a new {@link QueryResponse}. The returned
     * {@link QueryResponseAction} may be used to modify the request and to set each required field. If any required
     * field is missing the server will reject the request and respond with an error.
     * <p> If the operation is successful, the created QueryResponse will also be put into cache, if not already present.
     * @return Action that provides the newly crated {@link QueryResponse} on completion.
     */
    @NotNull QueryResponseAction createQueryResponse();

    /**
     * Creates an Action with the Provider request to create a new {@link TemplateForm}. The returned
     * {@link TemplateFormAction} may be used to modify the request and to set each required field. If any required
     * field is missing the server will reject the request and respond with an error.
     * <p> If the operation is successful, the created Form will also be put into cache, if not already present.
     * @return Action that provides the newly crated {@link TemplateForm} on completion.
     */
    @NotNull TemplateFormAction createTemplateForm();

    /**
     * Creates an Action with the Provider request to create a new {@link TextElement}. The returned
     * {@link TextElementAction} may be used to modify the request and to set each required field. If any required field
     * is missing the server will reject the request and respond with an error.
     * <p> If the operation is successful, the created Element will also be put into cache, if not already present.
     * @return Action that provides the newly crated {@link TextElement} on completion.
     */
    @NotNull TextElementAction createTextElement();

    // MESSAGES

    /**
     * Creates an Action with the Provider request to create a new {@link Attachment}. The returned
     * {@link AttachmentAction} may be used to modify the request and to set each required field. If any required
     * field is missing the server will reject the request and respond with an error.
     * <p> If the operation is successful, the created Attachment will also be put into cache, if not already present.
     * @return Action that provides the newly crated {@link DiscordChannel} on completion.
     */
    @NotNull AttachmentAction createAttachment();

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
