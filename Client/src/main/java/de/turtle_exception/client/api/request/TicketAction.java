package de.turtle_exception.client.api.request;

import de.turtle_exception.client.api.TicketState;
import de.turtle_exception.client.api.entities.Ticket;
import de.turtle_exception.client.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;

@SuppressWarnings("unused")
public interface TicketAction extends Action<Ticket> {
    TicketAction setState(TicketState state);

    TicketAction setTitle(String title);

    TicketAction setCategory(String category);

    TicketAction setTags(@NotNull Collection<String> tags);

    default TicketAction setTags(@NotNull String... tags) {
        return this.setTags(Arrays.asList(tags));
    }

    TicketAction addTag(String tag);

    TicketAction removeTag(String tag);

    TicketAction setDiscordChannelId(long channel);

    default TicketAction setDiscordChannel(@NotNull GuildMessageChannel channel) {
        return this.setDiscordChannelId(channel.getIdLong());
    }

    TicketAction setUserIds(@NotNull Collection<Long> users);

    default TicketAction setUsers(@NotNull Collection<User> users) {
        return this.setUserIds(users.stream().map(User::getId).toList());
    }

    default TicketAction setUserIds(@NotNull Long... users) {
        return this.setUserIds(Arrays.asList(users));
    }

    default TicketAction setUsers(@NotNull User... users) {
        return this.setUsers(Arrays.asList(users));
    }

    TicketAction addUser(long user);

    default TicketAction addUser(@NotNull User user) {
        return this.addUser(user.getId());
    }

    TicketAction removeUser(long user);

    default TicketAction removeUser(@NotNull User user) {
        return this.removeUser(user.getId());
    }
}
