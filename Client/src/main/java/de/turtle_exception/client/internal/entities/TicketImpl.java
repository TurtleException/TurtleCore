package de.turtle_exception.client.internal.entities;

import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TicketState;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.Ticket;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.util.TurtleSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class TicketImpl extends TurtleImpl implements Ticket {
    private TicketState state;
    private String title;
    private String category;
    private long discordChannel;

    private Set<String> tags = Sets.newConcurrentHashSet();
    private TurtleSet<User> users;

    TicketImpl(@NotNull TurtleClient client, long id, TicketState state, String title, String category, long discordChannel, Collection<String> tags, TurtleSet<User> users) {
        super(client, id);

        this.state = state;
        this.title = title;
        this.category = category;
        this.discordChannel = discordChannel;

        this.tags.addAll(tags);
        this.users = users;
    }

    @Override
    public synchronized @NotNull TicketImpl handleUpdate(@NotNull JsonObject json) {
        this.apply(json, "state", element -> { this.state = TicketState.of(element.getAsByte()); });
        this.apply(json, "title", element -> { this.title = element.getAsString(); });
        this.apply(json, "category", element -> { this.category = element.getAsString(); });
        this.apply(json, "discord_channel", element -> { this.discordChannel = element.getAsLong(); });
        this.apply(json, "discord", element -> {
            Set<String> set = Sets.newConcurrentHashSet();
            for (JsonElement entry : element.getAsJsonArray())
                set.add(entry.getAsString());
            this.tags = set;
        });
        this.apply(json, "users", element -> {
            TurtleSet<User> set = new TurtleSet<>();
            for (JsonElement entry : element.getAsJsonArray())
                set.add(client.getUserById(entry.getAsLong()));
            this.users = set;
        });
        return this;
    }

    /* - STATE - */

    @Override
    public @NotNull TicketState getState() {
        return this.state;
    }

    @Override
    public @NotNull Action<Ticket> modifyState(@NotNull TicketState state) {
        return getClient().getProvider().patch(this, "state", state.getCode()).andThenParse(Ticket.class);
    }

    /* - TITLE - */

    @Override
    public @Nullable String getTitle() {
        return this.title;
    }

    @Override
    public @NotNull Action<Ticket> modifyTitle(@Nullable String title) {
        return getClient().getProvider().patch(this, "title", String.valueOf(title)).andThenParse(Ticket.class);
    }

    /* - CATEGORY - */

    @Override
    public @NotNull String getCategory() {
        return this.category;
    }

    @Override
    public @NotNull Action<Ticket> modifyCategory(@NotNull String category) {
        return getClient().getProvider().patch(this, "category", category).andThenParse(Ticket.class);
    }

    /* - TAGS - */

    @Override
    public @NotNull List<String> getTags() {
        return List.copyOf(tags);
    }

    @Override
    public @NotNull Action<Ticket> addTag(@NotNull String tag) {
        JsonArray arr = new JsonArray();
        for (String aTag : tags)
            arr.add(aTag);
        arr.add(tag);
        return getClient().getProvider().patch(this, "tags", arr).andThenParse(Ticket.class);
    }

    @Override
    public @NotNull Action<Ticket> removeTag(@NotNull String tag) {
        JsonArray arr = new JsonArray();
        for (String aTag : tags)
            if (!aTag.equals(tag))
                arr.add(aTag);
        return getClient().getProvider().patch(this, "tags", arr).andThenParse(Ticket.class);
    }

    /* - DISCORD - */

    @Override
    public long getDiscordChannelId() {
        return this.discordChannel;
    }

    @Override
    public @NotNull Action<Ticket> modifyDiscordChannel(long channel) {
        return getClient().getProvider().patch(this, "channel", channel).andThenParse(Ticket.class);
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
    public @NotNull Action<Ticket> addUser(long user) {
        JsonArray arr = new JsonArray();
        for (User aUser : users)
            arr.add(aUser.getId());
        arr.add(user);
        return getClient().getProvider().patch(this, "users", arr).andThenParse(Ticket.class);
    }

    @Override
    public @NotNull Action<Ticket> removeUser(long user) {
        JsonArray arr = new JsonArray();
        for (User aUser : users)
            if (aUser.getId() != user)
                arr.add(aUser.getId());
        return getClient().getProvider().patch(this, "users", arr).andThenParse(Ticket.class);
    }
}
