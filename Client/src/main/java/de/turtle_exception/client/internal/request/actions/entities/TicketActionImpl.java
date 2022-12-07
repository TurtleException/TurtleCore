package de.turtle_exception.client.internal.request.actions.entities;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.turtle_exception.client.api.entities.Ticket;
import de.turtle_exception.client.api.entities.attributes.TicketState;
import de.turtle_exception.client.api.request.entities.TicketAction;
import de.turtle_exception.client.internal.Provider;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.internal.request.actions.EntityAction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public class TicketActionImpl extends EntityAction<Ticket> implements TicketAction {
    protected TicketState state = TicketState.UNDEFINED;
    protected String title;
    protected String category;
    protected ArrayList<String> tags = new ArrayList<>();
    protected Long discordChannel;
    protected ArrayList<Long> users = new ArrayList<>();

    @SuppressWarnings("CodeBlock2Expr")
    public TicketActionImpl(@NotNull Provider provider) {
        super(provider, Ticket.class);

        this.checks.add(json -> { json.get(Keys.Ticket.STATE).getAsByte(); });
        this.checks.add(json -> { json.get(Keys.Ticket.TITLE).getAsString(); });
        this.checks.add(json -> { json.get(Keys.Ticket.CATEGORY).getAsString(); });
        this.checks.add(json -> {
            JsonArray arr = json.get(Keys.Ticket.TAGS).getAsJsonArray();
            for (JsonElement entry : arr)
                entry.getAsString();
        });
        this.checks.add(json -> { json.get(Keys.Ticket.DISCORD_CHANNEL).getAsLong(); });
        this.checks.add(json -> {
            JsonArray arr = json.get(Keys.Ticket.USERS).getAsJsonArray();
            for (JsonElement entry : arr)
                entry.getAsLong();
        });
    }

    @Override
    protected void updateContent() {
        this.content = new JsonObject();
        this.content.addProperty(Keys.Ticket.STATE, state.getCode());
        this.content.addProperty(Keys.Ticket.TITLE, title);
        this.content.addProperty(Keys.Ticket.CATEGORY, category);

        JsonArray tags = new JsonArray();
        for (String tag : this.tags)
            tags.add(tag);
        this.content.add(Keys.Ticket.TAGS, tags);

        this.content.addProperty(Keys.Ticket.DISCORD_CHANNEL, discordChannel);

        JsonArray users = new JsonArray();
        for (Long user : this.users)
            users.add(user);
        this.content.add(Keys.Ticket.USERS, users);
    }

    /* - - - */

    @Override
    public TicketAction setState(TicketState state) {
        this.state = state;
        return this;
    }

    @Override
    public TicketAction setTitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    public TicketAction setCategory(String category) {
        this.category = category;
        return this;
    }

    @Override
    public TicketAction setTags(@NotNull Collection<String> tags) {
        this.tags = new ArrayList<>(tags);
        return this;
    }

    @Override
    public TicketAction addTag(String tag) {
        this.tags.add(tag);
        return this;
    }

    @Override
    public TicketAction removeTag(String tag) {
        this.tags.remove(tag);
        return this;
    }

    @Override
    public TicketAction setDiscordChannelId(long channel) {
        this.discordChannel = channel;
        return this;
    }

    @Override
    public TicketAction setUserIds(@NotNull Collection<Long> users) {
        this.users = new ArrayList<>(users);
        return this;
    }

    @Override
    public TicketAction addUser(long user) {
        this.users.add(user);
        return this;
    }

    @Override
    public TicketAction removeUser(long user) {
        this.users.remove(user);
        return this;
    }
}
