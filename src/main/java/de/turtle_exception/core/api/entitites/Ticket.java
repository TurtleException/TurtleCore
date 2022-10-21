package de.turtle_exception.core.api.entitites;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface Ticket extends Turtle {
    int getState();

    @Nullable String getTitle();

    @NotNull String getCategory();

    @NotNull List<String> getTags();

    long getDiscordChannelId();

    @NotNull List<User> getUsers();
}
