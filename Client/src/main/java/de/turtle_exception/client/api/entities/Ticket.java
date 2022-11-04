package de.turtle_exception.client.api.entities;

import de.turtle_exception.client.api.TicketState;
import de.turtle_exception.client.api.entities.attribute.IUserContainer;
import de.turtle_exception.client.api.requests.Action;
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
    /* - STATE - */

    @NotNull TicketState getState();

    @Key(name = "state")
    default byte getStateCode() {
        return this.getState().getCode();
    }

    @NotNull Action<Void> modifyState(@NotNull TicketState state);

    /* - TITLE - */

    @Key(name = "title")
    @Nullable String getTitle();

    @NotNull Action<Void> modifyTitle(@Nullable String title);

    /* - CATEGORY - */

    @Key(name = "category")
    @NotNull String getCategory();

    @NotNull Action<Void> modifyCategory(@NotNull String category);

    /* - TAGS - */

    @Key(name = "tags", relation = Relation.MANY_TO_MANY)
    @NotNull List<String> getTags();

    @NotNull Action<Void> addTag(@NotNull String tag);

    @NotNull Action<Void> removeTag(@NotNull String tag);

    /* - DISCORD - */

    @Key(name = "discord_channel")
    long getDiscordChannelId();

    @NotNull Action<Void> modifyDiscordChannel(long channel);

    default @Nullable GuildMessageChannel getDiscordChannel() throws IllegalStateException {
        JDA jda = this.getClient().getJDA();

        if (jda == null)
            throw new IllegalStateException("No JDA instance registered");

        return jda.getChannelById(GuildMessageChannel.class, this.getDiscordChannelId());
    }

    /* - USERS - */

    @Key(name = "ticket_users", relation = Relation.MANY_TO_MANY)
    @NotNull List<User> getUsers();

    @NotNull Action<Void> addUser(@NotNull User user);

    @NotNull Action<Void> removeUser(@NotNull User user);
}
