package de.turtle_exception.core.api.entitites;

import de.turtle_exception.core.internal.data.annotations.Key;
import de.turtle_exception.core.internal.data.annotations.Relation;
import de.turtle_exception.core.internal.data.annotations.Resource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Resource(name = "tickets")
@SuppressWarnings("unused")
public interface Ticket extends Turtle {
    @Key(name = "state")
    int getState();

    @Key(name = "title")
    @Nullable String getTitle();

    @Key(name = "category")
    @NotNull String getCategory();

    @Key(name = "tags", relation = Relation.MANY_TO_MANY)
    @NotNull List<String> getTags();

    @Key(name = "discord_channel")
    long getDiscordChannelId();

    @Key(name = "ticket_users", relation = Relation.MANY_TO_MANY)
    @NotNull List<User> getUsers();
}
