package de.turtle_exception.client.api.entities;

import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.attributes.TicketState;
import de.turtle_exception.client.api.entities.containers.TurtleContainer;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.data.annotations.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * A Ticket is a request to the team regarding a bug, feature-request or support-issue. It is possible for multiple
 * {@link User Users} to be part of a Ticket, though by default only the initial author (the user who opened the Ticket)
 * has access. Tickets may also have a title and/or multiple tags. Tickets can be organized by their
 * {@link TicketState State} and usually have a designated channel on Discord.
 */
@Resource(path = "tickets", builder = "buildTicket")
@SuppressWarnings("unused")
public interface Ticket extends Turtle, TurtleContainer<User> {
    @Override
    default @NotNull Action<Ticket> update() {
        return this.getClient().retrieveTurtle(this.getId(), Ticket.class);
    }

    /* - STATE - */

    /**
     * Provides the state of this Ticket. See {@link TicketState} documentation for more information.
     * @return The Ticket state.
     */
    @NotNull TicketState getState();

    /**
     * Provides the state of this Ticket in its {@code byte} representation. This method exists mainly for serialization
     * purposes, as it is a rather inefficient shortcut for {@code Ticket.getState().getCode()}.
     * @return The Ticket state as a {@code byte}.
     */
    @Key(name = Keys.Ticket.STATE, sqlType = Types.Ticket.STATE)
    default byte getStateCode() {
        return this.getState().getCode();
    }

    /**
     * Creates an Action with the instruction to modify this Ticket's state and change it to the provided argument.
     * @param state New Ticket state.
     * @return Action that provides the modified {@link Ticket} on completion.
     */
    @NotNull Action<Ticket> modifyState(@NotNull TicketState state);

    /* - TITLE - */

    /**
     * Provides the title of this Ticket. Ticket titles are not guaranteed to be unique and rather function as a description.
     * Uniqueness can only be checked by {@link Ticket#getId()}.
     * <p> Titles also may be {@code null}, if they have not been set.
     * @return The Ticket title (possibly {@code null}).
     */
    @Key(name = Keys.Ticket.TITLE, sqlType = Types.Ticket.TITLE)
    @Nullable String getTitle();

    /**
     * Creates an Action with the instruction to modify this Ticket's title and change it to the provided String.
     * @param title New Ticket title.
     * @return Action that provides the modified {@link Ticket} on completion.
     */
    @NotNull Action<Ticket> modifyTitle(@Nullable String title);

    /* - CATEGORY - */

    /**
     * Provides the category of this Ticket.
     * @return The Ticket category.
     */
    @Key(name = Keys.Ticket.CATEGORY, relation = Relation.MANY_TO_ONE, sqlType = Types.Ticket.CATEGORY)
    @NotNull String getCategory();

    /**
     * Creates an Action with the instruction to modify this Ticket's category and change it to the provided String.
     * @param category New Ticket category.
     * @return Action that provides the modified {@link Ticket} on completion.
     */
    @NotNull Action<Ticket> modifyCategory(@NotNull String category);

    /* - TAGS - */

    /**
     * Provides a List of this Ticket's tags.
     * <p> Tags are custom Strings that can be assigned by the Ticket author(s) or by team, moderation and bots.
     * @return List of Ticket tags.
     */
    @Key(name = Keys.Ticket.TAGS, relation = Relation.MANY_TO_MANY, sqlType = Types.Ticket.TAGS)
    @Relational(table = "ticket_tags", self = "ticket", foreign = "tag", type = String.class)
    @NotNull List<String> getTags();

    /**
     * Creates an Action with the instruction to add the provided String to this Ticket's list of tags.
     * @param tag New tag.
     * @return Action that provides the modified {@link Ticket} on completion.
     */
    @NotNull Action<Ticket> addTag(@NotNull String tag);

    /**
     * Creates an Action with the instruction to remove the String from this Ticket's list of tags.
     * @param tag Tag to remove.
     * @return Action that provides the modified {@link Ticket} on completion.
     */
    @NotNull Action<Ticket> removeTag(@NotNull String tag);

    /* - DISCORD - */

    /**
     * Provides the Discord channel if of this Ticket.
     * @return Snowflake channel id.
     */
    @Key(name = Keys.Ticket.DISCORD_CHANNEL, sqlType = Types.Ticket.DISCORD_CHANNEL)
    long getDiscordChannelId();

    /**
     * Creates an Action with the instruction to modify this Ticket's Discord channel id and change it to the provided id.
     * @param channel New Discord channel id.
     * @return Action that provides the modified {@link Ticket} on completion.
     */
    @NotNull Action<Ticket> modifyDiscordChannel(long channel);

    /**
     * Attempts to retrieve the {@link GuildMessageChannel} represented by {@link Ticket#getDiscordChannelId()} from the
     * {@link JDA} instance registered to the {@link TurtleClient}.
     * @return GuildMessageChannel representation of {@link Ticket#getDiscordChannelId()}.
     * @throws IllegalStateException if no {@link JDA} instance has been registered to the {@link TurtleClient}.
     */
    default @Nullable GuildMessageChannel getDiscordChannel() throws IllegalStateException {
        JDA jda = this.getClient().getJDA();

        if (jda == null)
            throw new IllegalStateException("No JDA instance registered");

        return jda.getChannelById(GuildMessageChannel.class, this.getDiscordChannelId());
    }

    /* - USERS - */

    @Override
    default @NotNull List<User> getTurtles() {
        return this.getUsers();
    }

    /**
     * Provides a List of all {@link User Users} that have access to this Ticket.
     * <p> A Ticket can have multiple Users; A User can also have access to multiple Groups.
     * <p> Team members, moderation and bots are not included in this list unless they have explicitly been added.
     * @return List of Users.
     */
    @Key(name = Keys.Ticket.USERS, relation = Relation.MANY_TO_MANY, sqlType = Types.Ticket.USERS)
    @Relational(table = "ticket_users", self = "ticket", foreign = "user", type = User.class)
    @NotNull List<User> getUsers();

    /**
     * Creates an Action with the instruction to add the provided id to the list of Users that have access to this Ticket.
     * <p> The provided {@code long} should be a representation of a {@link User} id.
     * @param user Turtle ID of a User.
     * @return Action that provides the modified {@link Ticket} on completion.
     */
    @NotNull Action<Ticket> addUser(long user);

    /**
     * Creates an Action with the instruction to add the provided {@link User} to the list of Users that have access to
     * this Ticket.
     * <p> This is a shortcut for {@code Ticket.addUser(user.getId())}.
     * @param user A User.
     * @return Action that provides the modified {@link Ticket} on completion.
     */
    default @NotNull Action<Ticket> addUser(@NotNull User user) {
        return this.addUser(user.getId());
    }

    /**
     * Creates an Action with the instruction to remove the provided id from the list of Users that have access to this
     * Ticket.
     * <p> The provided {@code long} should be a representation of a {@link User} id.
     * @param user Turtle ID of a User.
     * @return Action that provides the modified {@link Ticket} on completion.
     */
    @NotNull Action<Ticket> removeUser(long user);

    /**
     * Creates an Action with the instruction to remove the provided {@link User} from the list of Users that have
     * access to this Ticket.
     * <p> This is a shortcut for {@code Ticket.removeUser(user.getId())}.
     * @param user A User.
     * @return Action that provides the modified {@link Ticket} on completion.
     */
    default @NotNull Action<Ticket> removeUser(@NotNull User user) {
        return this.removeUser(user.getId());
    }
}
