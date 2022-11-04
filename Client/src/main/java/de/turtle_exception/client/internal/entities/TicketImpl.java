package de.turtle_exception.client.internal.entities;

import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.Ticket;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.TicketState;
import de.turtle_exception.client.api.requests.Action;
import de.turtle_exception.client.internal.net.route.Routes;
import de.turtle_exception.client.internal.util.TurtleSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class TicketImpl implements Ticket {
    private final TurtleClient client;
    private final long id;

    private TicketState state;
    private String title;
    private String category;
    private long discordChannel;

    private final Set<String> tags = Sets.newConcurrentHashSet();
    private final TurtleSet<User> users;

    TicketImpl(TurtleClient client, long id, TicketState state, String title, String category, long discordChannel, Collection<String> tags, TurtleSet<User> users) {
        this.client = client;
        this.id = id;

        this.state = state;
        this.title = title;
        this.category = category;
        this.discordChannel = discordChannel;

        this.tags.addAll(tags);
        this.users = users;
    }

    @Override
    public @NotNull TurtleClient getClient() {
        return this.client;
    }

    @Override
    public long getId() {
        return this.id;
    }

    /* - STATE - */

    @Override
    public @NotNull TicketState getState() {
        return this.state;
    }

    @Override
    public @NotNull Action<Void> modifyState(@NotNull TicketState state) {
        JsonObject json = new JsonObject();
        json.addProperty("state", state.getCode());
        return new ActionImpl<>(client, Routes.Ticket.MODIFY.compile(json, this.id), null);
    }

    /* - TITLE - */

    @Override
    public @Nullable String getTitle() {
        return this.title;
    }

    @Override
    public @NotNull Action<Void> modifyTitle(@Nullable String title) {
        JsonObject json = new JsonObject();
        json.addProperty("title", String.valueOf(title));
        return new ActionImpl<>(client, Routes.Ticket.MODIFY.compile(json, this.id), null);
    }

    /* - CATEGORY - */

    @Override
    public @NotNull String getCategory() {
        return this.category;
    }

    @Override
    public @NotNull Action<Void> modifyCategory(@NotNull String category) {
        JsonObject json = new JsonObject();
        json.addProperty("category", category);
        return new ActionImpl<>(client, Routes.Ticket.MODIFY.compile(json, this.id), null);
    }

    /* - TAGS - */

    @Override
    public @NotNull List<String> getTags() {
        return List.copyOf(tags);
    }

    @Override
    public @NotNull Action<Void> addTag(@NotNull String tag) {
        return new ActionImpl<>(client, Routes.Ticket.ADD_TAG.compile(null, this.id, tag), null);
    }

    @Override
    public @NotNull Action<Void> removeTag(@NotNull String tag) {
        return new ActionImpl<>(client, Routes.Ticket.DEL_TAG.compile(null, this.id, tag), null);
    }

    /* - DISCORD - */

    @Override
    public long getDiscordChannelId() {
        return this.discordChannel;
    }

    @Override
    public @NotNull Action<Void> modifyDiscordChannel(long channel) {
        JsonObject json = new JsonObject();
        json.addProperty("channel", channel);
        return new ActionImpl<>(client, Routes.Ticket.MODIFY.compile(json, this.id), null);
    }

    /* - USERS - */

    @Override
    public @NotNull List<User> getUsers() {
        return List.copyOf(users);
    }

    public @NotNull TurtleSet<User> getUserSet() {
        return users;
    }

    @Override
    public @Nullable User getUserById(long id) {
        return users.get(id);
    }

    @Override
    public @NotNull Action<Void> addUser(@NotNull User user) {
        return new ActionImpl<>(client, Routes.Ticket.ADD_USER.compile(null, this.id, user), null);
    }

    @Override
    public @NotNull Action<Void> removeUser(@NotNull User user) {
        return new ActionImpl<>(client, Routes.Ticket.ADD_USER.compile(null, this.id, user), null);
    }
}
