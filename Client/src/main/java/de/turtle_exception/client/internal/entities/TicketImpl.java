package de.turtle_exception.client.internal.entities;

import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.turtle_exception.client.api.entities.attributes.TicketState;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.Ticket;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.event.entities.ticket.TicketUpdateCategoryEvent;
import de.turtle_exception.client.api.event.entities.ticket.TicketUpdateDiscordChannelEvent;
import de.turtle_exception.client.api.event.entities.ticket.TicketUpdateStateEvent;
import de.turtle_exception.client.api.event.entities.ticket.TicketUpdateTitleEvent;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.event.UpdateHelper;
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
        this.apply(json, Keys.Ticket.STATE, element -> {
            TicketState old = this.state;
            this.state = TicketState.of(element.getAsByte());
            this.fireEvent(new TicketUpdateStateEvent(this, old, this.state));
        });
        this.apply(json, Keys.Ticket.TITLE, element -> {
            String old = this.title;
            this.title = element.getAsString();
            this.fireEvent(new TicketUpdateTitleEvent(this, old, this.title));
        });
        this.apply(json, Keys.Ticket.CATEGORY, element -> {
            String old = this.category;
            this.category = element.getAsString();
            this.fireEvent(new TicketUpdateCategoryEvent(this, old, this.category));
        });
        this.apply(json, Keys.Ticket.DISCORD_CHANNEL, element -> {
            long old = this.discordChannel;
            this.discordChannel = element.getAsLong();
            this.fireEvent(new TicketUpdateDiscordChannelEvent(this, old, this.discordChannel));
        });
        this.apply(json, Keys.Ticket.TAGS, element -> {
            Set<String> old = this.tags;
            Set<String> set = Sets.newConcurrentHashSet();
            for (JsonElement entry : element.getAsJsonArray())
                set.add(entry.getAsString());
            this.tags = set;
            UpdateHelper.ofTicketTags(this, old, set);
        });
        this.apply(json, Keys.Ticket.USERS, element -> {
            TurtleSet<User> old = this.users;
            TurtleSet<User> set = new TurtleSet<>();
            for (JsonElement entry : element.getAsJsonArray())
                set.add(client.getUserById(entry.getAsLong()));
            this.users = set;
            UpdateHelper.ofTicketUsers(this, old, set);
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
        return getClient().getProvider().patch(this, Keys.Ticket.STATE, state.getCode()).andThenParse(Ticket.class);
    }

    /* - TITLE - */

    @Override
    public @Nullable String getTitle() {
        return this.title;
    }

    @Override
    public @NotNull Action<Ticket> modifyTitle(@Nullable String title) {
        return getClient().getProvider().patch(this, Keys.Ticket.TITLE, title).andThenParse(Ticket.class);
    }

    /* - CATEGORY - */

    @Override
    public @NotNull String getCategory() {
        return this.category;
    }

    @Override
    public @NotNull Action<Ticket> modifyCategory(@NotNull String category) {
        return getClient().getProvider().patch(this, Keys.Ticket.CATEGORY, category).andThenParse(Ticket.class);
    }

    /* - TAGS - */

    @Override
    public @NotNull List<String> getTags() {
        return List.copyOf(tags);
    }

    @Override
    public @NotNull Action<Ticket> addTag(@NotNull String tag) {
        return getClient().getProvider().patchEntryAdd(this, Keys.Ticket.TAGS, tag).andThenParse(Ticket.class);
    }

    @Override
    public @NotNull Action<Ticket> removeTag(@NotNull String tag) {
        return getClient().getProvider().patchEntryDel(this, Keys.Ticket.TAGS, tag).andThenParse(Ticket.class);
    }

    /* - DISCORD - */

    @Override
    public long getDiscordChannelId() {
        return this.discordChannel;
    }

    @Override
    public @NotNull Action<Ticket> modifyDiscordChannel(long channel) {
        return getClient().getProvider().patch(this, Keys.Ticket.DISCORD_CHANNEL, channel).andThenParse(Ticket.class);
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
        return getClient().getProvider().patchEntryAdd(this, Keys.Ticket.USERS, user).andThenParse(Ticket.class);
    }

    @Override
    public @NotNull Action<Ticket> removeUser(long user) {
        return getClient().getProvider().patchEntryDel(this, Keys.Ticket.USERS, user).andThenParse(Ticket.class);
    }
}
