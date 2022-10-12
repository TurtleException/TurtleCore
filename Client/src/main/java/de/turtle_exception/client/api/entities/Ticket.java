package de.turtle_exception.client.api.entities;

import de.turtle_exception.client.api.entities.attribute.IUserContainer;
import de.turtle_exception.core.data.TicketState;
import de.turtle_exception.client.api.requests.Action;
import de.turtle_exception.core.data.resource.Resource;
import de.turtle_exception.core.data.resource.ResourceKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Resource(path = "tickets")
public interface Ticket extends Turtle, IUserContainer {
    /* - STATE - */

    @ResourceKey(path = "state")
    @NotNull TicketState getState();

    @NotNull Action<Void> modifyState(@NotNull TicketState state);

    /* - TITLE - */

    @ResourceKey(path = "title")
    @Nullable String getTitle();

    @NotNull Action<Void> modifyTitle(@Nullable String title);

    /* - CATEGORY - */

    @ResourceKey(path = "category")
    @NotNull String getCategory();

    @NotNull Action<Void> modifyCategory(@NotNull String category);

    /* - TAGS - */

    @NotNull List<String> getTags();

    @NotNull Action<Void> addTag(@NotNull String tag);

    @NotNull Action<Void> removeTag(@NotNull String tag);

    /* - DISCORD - */

    @ResourceKey(path = "discord_channel")
    long getDiscordChannelId();

    @NotNull Action<Void> modifyDiscordChannel(long channel);

    /* - USERS - */

    @NotNull List<User> getUsers();

    @NotNull Action<Void> addUser(@NotNull User user);

    @NotNull Action<Void> removeUser(@NotNull User user);
}
