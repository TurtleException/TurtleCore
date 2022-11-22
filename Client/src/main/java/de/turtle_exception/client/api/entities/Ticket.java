package de.turtle_exception.client.api.entities;

import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.api.TicketState;
import de.turtle_exception.client.api.entities.attribute.IUserContainer;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.data.annotations.Key;
import de.turtle_exception.client.internal.data.annotations.Relation;
import de.turtle_exception.client.internal.data.annotations.Resource;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Resource(path = "tickets", builder = "buildTicket")
public interface Ticket extends Turtle, IUserContainer {
    @Override
    default @NotNull Action<Ticket> update() {
        return this.getClient().retrieveTicket(this.getId());
    }

    /* - STATE - */

    @NotNull TicketState getState();

    @Key(name = Keys.Ticket.STATE)
    default byte getStateCode() {
        return this.getState().getCode();
    }

    @NotNull Action<Ticket> modifyState(@NotNull TicketState state);

    /* - TITLE - */

    @Key(name = Keys.Ticket.TITLE)
    @Nullable String getTitle();

    @NotNull Action<Ticket> modifyTitle(@Nullable String title);

    /* - CATEGORY - */

    @Key(name = Keys.Ticket.CATEGORY)
    @NotNull String getCategory();

    @NotNull Action<Ticket> modifyCategory(@NotNull String category);

    /* - TAGS - */

    @Key(name = Keys.Ticket.TAGS, relation = Relation.MANY_TO_MANY)
    @NotNull List<String> getTags();

    @NotNull Action<Ticket> addTag(@NotNull String tag);

    @NotNull Action<Ticket> removeTag(@NotNull String tag);

    /* - DISCORD - */

    @Key(name = Keys.Ticket.DISCORD_CHANNEL)
    long getDiscordChannelId();

    @NotNull Action<Ticket> modifyDiscordChannel(long channel);

    default @Nullable GuildMessageChannel getDiscordChannel() throws IllegalStateException {
        JDA jda = this.getClient().getJDA();

        if (jda == null)
            throw new IllegalStateException("No JDA instance registered");

        return jda.getChannelById(GuildMessageChannel.class, this.getDiscordChannelId());
    }

    /* - USERS - */

    @Key(name = Keys.Ticket.USERS, relation = Relation.MANY_TO_MANY)
    @NotNull List<User> getUsers();

    @NotNull Action<Ticket> addUser(long user);

    default @NotNull Action<Ticket> addUser(@NotNull User user) {
        return this.addUser(user.getId());
    }

    @NotNull Action<Ticket> removeUser(long user);

    default @NotNull Action<Ticket> removeUser(@NotNull User user) {
        return this.removeUser(user.getId());
    }
}
