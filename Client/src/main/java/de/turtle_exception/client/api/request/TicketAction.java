package de.turtle_exception.client.api.request;

import de.turtle_exception.client.api.TicketState;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.Ticket;
import de.turtle_exception.client.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;

/**
 * A TicketAction is an Action that requests the creation of a new {@link Ticket}, according to the arguments this Action
 * holds. If any required field is missing the server will reject the request and respond with an error. Required fields
 * are all attributes that are not a subclass of {@link Collection}, as these are set to an empty Collection by default.
 * @see TurtleClient#createTicket()
 */
@SuppressWarnings("unused")
public interface TicketAction extends Action<Ticket> {
    /**
     * Sets the {@link TicketState} of this Ticket to the provided state.
     * @param state Ticket state.
     * @return This TicketAction for chaining convenience.
     */
    TicketAction setState(TicketState state);

    /**
     * Sets the title of this Ticket to the provided String.
     * @param title Ticket title.
     * @return This TicketAction for chaining convenience.
     */
    TicketAction setTitle(String title);

    /**
     * Sets the category of this Ticket to the provided String.
     * @param category Ticket category.
     * @return This TicketAction for chaining convenience.
     */
    TicketAction setCategory(String category);

    /**
     * Sets the List of tags to the provided Collection. The existing List will be overwritten.
     * @param tags Collection of tags.
     * @return This TicketAction for chaining convenience.
     */
    TicketAction setTags(@NotNull Collection<String> tags);

    /**
     * Sets the List of tags to the provided Array. The existing List will be overwritten.
     * @param tags Array of tags.
     * @return This TicketAction for chaining convenience.
     */
    default TicketAction setTags(@NotNull String... tags) {
        return this.setTags(Arrays.asList(tags));
    }

    /**
     * Adds the provided String to the List of tags for this Ticket.
     * @param tag Some tag.
     * @return This TicketAction for chaining convenience.
     */
    TicketAction addTag(String tag);

    /**
     * Removes the provided String from the List of tags for this Ticket.
     * @param tag Some tag.
     * @return This TicketAction for chaining convenience.
     */
    TicketAction removeTag(String tag);

    /**
     * Sets the Discord channel id of this Ticket to the provided {@code long}.
     * @param channel Snowflake id.
     * @return This TicketAction for chaining convenience.
     */
    TicketAction setDiscordChannelId(long channel);

    /**
     * Sets the Discord channel of this Ticket to the provided {@link GuildMessageChannel}.
     * @param channel Discord channel.
     * @return This TicketAction for chaining convenience.
     */
    default TicketAction setDiscordChannel(@NotNull GuildMessageChannel channel) {
        return this.setDiscordChannelId(channel.getIdLong());
    }

    /**
     * Sets the List of ids that each represent a {@link User} that has access to this Ticket.
     * The existing List will be overwritten.
     * @param users Collection of User ids.
     * @return This TicketAction for chaining convenience.
     */
    TicketAction setUserIds(@NotNull Collection<Long> users);

    /**
     * Sets the List of {@link User Users} that have access to this Ticket.
     * The existing List will be overwritten.
     * @param users Collection of Users.
     * @return This TicketAction for chaining convenience.
     */
    default TicketAction setUsers(@NotNull Collection<User> users) {
        return this.setUserIds(users.stream().map(User::getId).toList());
    }

    /**
     * Sets the List of ids that each represent a {@link User} that has access to this Ticket.
     * The existing List will be overwritten.
     * @param users Array of User ids.
     * @return This TicketAction for chaining convenience.
     */
    default TicketAction setUserIds(@NotNull Long... users) {
        return this.setUserIds(Arrays.asList(users));
    }

    /**
     * Sets the List of {@link User Users} that have access to this Ticket.
     * The existing List will be overwritten.
     * @param users Array of Users.
     * @return This TicketAction for chaining convenience.
     */
    default TicketAction setUsers(@NotNull User... users) {
        return this.setUsers(Arrays.asList(users));
    }

    /**
     * Adds the provided {@code long} to the List of ids that each represent a {@link User} that has access to this Ticket.
     * @param user User id.
     * @return This TicketAction for chaining convenience.
     */
    TicketAction addUser(long user);

    /**
     * Adds the provided {@link User} to the List Users that have access to this Ticket.
     * @param user Some User.
     * @return This TicketAction for chaining convenience.
     */
    default TicketAction addUser(@NotNull User user) {
        return this.addUser(user.getId());
    }

    /**
     * Removes the provided {@code long} from the List of ids that each represent a {@link User} that has access to this Ticket.
     * @param user User id.
     * @return This TicketAction for chaining convenience.
     */
    TicketAction removeUser(long user);

    /**
     * Removes the provided {@link User} from the List Users that have access to this Ticket.
     * @param user Some User.
     * @return This TicketAction for chaining convenience.
     */
    default TicketAction removeUser(@NotNull User user) {
        return this.removeUser(user.getId());
    }
}
