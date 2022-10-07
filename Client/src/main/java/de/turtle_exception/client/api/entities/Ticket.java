package de.turtle_exception.client.api.entities;

import de.turtle_exception.client.api.entities.attribute.IUserContainer;
import de.turtle_exception.client.api.requests.Action;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface Ticket extends Turtle, IUserContainer {
    /* - STATE - */

    byte getState();

    @NotNull Action<Void> modifyState(byte state);

    /* - TITLE - */

    @Nullable String getTitle();

    @NotNull Action<Void> modifyTitle(@Nullable String title);

    /* - CATEGORY - */

    @NotNull String getCategory();

    @NotNull Action<Void> modifyCategory(@NotNull String category);

    /* - TAGS - */

    @NotNull List<String> getTags();

    @NotNull Action<Void> addTag(@NotNull String tag);

    @NotNull Action<Void> removeTag(@NotNull String tag);

    /* - DISCORD - */

    long getDiscordChannelId();

    @NotNull Action<Void> modifyDiscordChannel(long channel);

    /* - USERS - */

    @NotNull List<User> getUsers();

    @NotNull Action<Void> addUser(@NotNull User user);

    @NotNull Action<Void> removeUser(@NotNull User user);
}
